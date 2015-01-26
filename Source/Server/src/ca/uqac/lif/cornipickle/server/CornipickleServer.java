/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cornipickle.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.net.URI;
import java.net.URLDecoder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.httpserver.InnerFileServer;
import ca.uqac.lif.httpserver.RequestCallback;
import ca.uqac.lif.util.FileReadWrite;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class CornipickleServer extends InnerFileServer
{
  /**
   * Return codes
   */
  public static final int ERR_OK = 0;
  public static final int ERR_PARSE = 2;
  public static final int ERR_IO = 3;
  public static final int ERR_ARGUMENTS = 4;
  public static final int ERR_RUNTIME = 6;
  public static final int ERR_GRAMMAR = 7;
  public static final int ERR_INPUT = 9;

  /**
   * Build string to identify versions
   */
  protected static final String VERSION_STRING = "0.0";
  protected static final String BUILD_STRING = "20150126";

  protected Interpreter m_interpreter;

  protected static String s_defaultServerName = "localhost";

  protected static int s_defaultPort = 10101;
  
  /**
   * Verbosity level for CLI
   */
  protected static int s_verbosity = 1;

  static byte[] s_dummyImage = readBytes(CornipickleServer.class.getResourceAsStream("resource/dummy-image.png"));

  public CornipickleServer()
  {
    super();
    // Instantiate Cornipickle interpreter
    m_interpreter = new Interpreter();
    // Update class reference
    m_referenceClass = this.getClass();
    // Register callbacks
    registerCallback(0, new PropertyAddCallback());
    registerCallback(0, new StatusPageCallback());
    registerCallback(0, new ProbeCallback());
    registerCallback(0, new DummyImageCallback());
  }

  public void readProperties(String filename)
  {
    try
    {
      String corni_file_contents = FileReadWrite.readFile(filename);
      m_interpreter.parseProperties(corni_file_contents);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    String server_name = s_defaultServerName;
    int server_port = s_defaultPort;

    // Parse command line arguments
    Options options = setupOptions();
    CommandLine c_line = setupCommandLine(args, options);
    assert c_line != null;
    if (c_line.hasOption("verbosity"))
    {
      s_verbosity = Integer.parseInt(c_line.getOptionValue("verbosity"));
    }
    if (s_verbosity > 0)
    {
      showHeader(System.out);
    }
    if (c_line.hasOption("version"))
    {
      System.err.println("(C) 2015 Sylvain Hallé et al., Université du Québec à Chicoutimi");
      System.err.println("This program comes with ABSOLUTELY NO WARRANTY.");
      System.err.println("This is a free software, and you are welcome to redistribute it");
      System.err.println("under certain conditions. See the file LICENSE for details.\n");
      System.exit(ERR_OK);
    }
    if (c_line.hasOption("h"))
    {
      showUsage(options);
      System.exit(ERR_OK);
    }
    if (c_line.hasOption("p"))
    {
      server_port = Integer.parseInt(c_line.getOptionValue("p"));
    }
    
    // The remaining arguments are the Cornipickle files to read
    CornipickleServer server = new CornipickleServer();
    @SuppressWarnings("unchecked")
    List<String> remaining_args = c_line.getArgList();
    for (String filename : remaining_args)
    {
      println(System.out, "Reading properties in " + filename, 1);
      server.readProperties(filename);
    }
    
    // Start server
    server.setServerName(server_name);
    server.startServer(server_port);
    println(System.out, "Server started on " + server_name + ":" + server_port, 1);

    // Terminate without error
    //System.exit(ERR_OK);
  }
  
  protected static void println(PrintStream out, String message, int verbosity_level)
  {
    if (verbosity_level >= s_verbosity)
    {
      out.println(message);
    }
  }

  protected class ProbeCallback extends RequestCallback
  {
    @Override
    public boolean fire(HttpExchange t)
    {
      URI u = t.getRequestURI();
      String path = u.getPath();
      return path.compareTo("/probe") == 0;
    }

    @Override
    public boolean process(HttpExchange t)
    {
      try
      {
        String witness_code = PackageFileReader.readPackageFile(m_referenceClass.getResourceAsStream(m_resourceFolder + "/witness.js"));
        String probe_code = PackageFileReader.readPackageFile(m_referenceClass.getResourceAsStream(m_resourceFolder + "/probe.js"));
        probe_code = probe_code.replace("%%WITNESS_CODE%%", escapeString(witness_code));
        probe_code = probe_code.replace("%%SERVER_NAME%%", getServerName() + ":" + s_defaultPort);
        sendResponse(t, HTTP_OK, probe_code);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      return true;
    }
  }
  
  protected class PropertyAddCallback extends RequestCallback
  {
    @Override
    public boolean fire(HttpExchange t)
    {
      URI u = t.getRequestURI();
      String path = u.getPath();
      String method = t.getRequestMethod();
      return method.compareToIgnoreCase("post") == 0 && 
          path.compareTo("/add") == 0;
    }

    @Override
    public boolean process(HttpExchange t)
    {
      StringBuilder page = new StringBuilder();
      page.append("<!DOCTYPE html>\n");
      page.append("<html>\n");
      page.append("<head>\n");
      page.append("<title>Cornipickle Properties</title>\n");
      page.append("<script src=\"http://code.jquery.com/jquery-1.11.2.min.js\"></script>\n");
      page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"screen.css\" />\n");
      page.append("</head>\n");
      page.append("<body>\n");
      
      // Read POST data
      InputStream is_post = t.getRequestBody();
      String post_data = streamToString(is_post);
      
      // Try to decode and parse it
      boolean success = true;
      try
      {
        post_data = URLDecoder.decode(post_data, "UTF-8");
        Map<String,String> params = queryToMap(post_data);
        String props = params.get("properties");
        if (props != null)
        {
          m_interpreter.parseProperties(props);
        }
      } 
      catch (ParseException e)
      {
        success = false;
        page.append("<h1>Add properties</h1>\n");
        page.append("<p>The properties could not be added.\n");
        page.append("Message from the parser:</p>");
        page.append("<blockquote>\n");
        page.append(e.toString());
        page.append("</blockquote>\n");
      }
      catch (UnsupportedEncodingException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
        success = false;
      }
      if (success)
      {
        page.append("<h1>Add properties</h1>\n");
        page.append("<p>The properties were successfully added.</p>");
      }
      page.append("<hr />\n");
      Date d = new Date();
      page.append(d);
      page.append("</body>\n</html>\n");
      String page_string = page.toString();
      // Disable caching on the client
      Headers h = t.getResponseHeaders();
      h.add("Pragma", "no-cache");
      h.add("Cache-Control", "no-cache, no-store, must-revalidate");
      h.add("Expires", "0"); 
      sendResponse(t, HTTP_OK, page_string);
      return true;
    }    
  }

  protected class DummyImageCallback extends RequestCallback
  {
    @Override
    public boolean fire(HttpExchange t)
    {
      URI u = t.getRequestURI();
      String path = u.getPath();
      return path.compareTo("/image") == 0;
    }

    @Override
    public boolean process(HttpExchange t)
    {
      URI uri = t.getRequestURI();
      Map<String,String> attributes = uriToMap(uri); 

      // Extract JSON from URL string
      String json_encoded = attributes.get("contents");
      if (json_encoded != null)
      {
        JsonElement j = null;
        // Parse JSON
        try
        {
          String json_decoded = URLDecoder.decode(json_encoded, "UTF-8");
          j = JsonParser.parse(json_decoded);
          System.out.println("JSON received");
          //System.out.println(json_decoded);
        }
        catch (JsonParseException e)
        {
          e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e)
        {
          e.printStackTrace();
        }
        if (j != null)
        {
          m_interpreter.evaluateAll(j);
        }
      }
      // Whatever happens, serve the dummy image
      sendResponse(t, HTTP_OK, s_dummyImage);
      return true;
    }
  }

  protected class StatusPageCallback extends RequestCallback
  {
    @Override
    public boolean fire(HttpExchange t)
    {
      URI u = t.getRequestURI();
      String path = u.getPath();
      return path.compareTo("/status") == 0;
    }

    @Override
    public boolean process(HttpExchange t)
    {
      StringBuilder page = new StringBuilder();
      page.append("<!DOCTYPE html>\n");
      page.append("<html>\n");
      page.append("<head>\n");
      page.append("<title>Cornipickle Status</title>\n");
      page.append("<script src=\"http://code.jquery.com/jquery-1.11.2.min.js\"></script>\n");
      page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"screen.css\" />\n");
      page.append("</head>\n");
      page.append("<body>\n");
      page.append("<h1>Cornipickle Status</h1>");
      Map<StatementMetadata,Interpreter.Verdict> verdicts = m_interpreter.getVerdicts();
      page.append("<ul class=\"verdicts\">\n");
      for (StatementMetadata key : verdicts.keySet())
      {
        Interpreter.Verdict v = verdicts.get(key);
        if (v == Interpreter.Verdict.TRUE)
        {
          page.append("<li class=\"true\">").append(key.get("name")).append("</li>");
        }
        else if (v == Interpreter.Verdict.FALSE)
        {
          page.append("<li class=\"false\">").append(key.get("name")).append("</li>");
        }
        else
        {
          page.append("<li class=\"inconclusive\">").append(key.get("name")).append("</li>");
        }
      }
      page.append("</ul>\n");
      page.append("\n<div id=\"add-properties\">\n");
      page.append("<h2>Add properties</h2>\n\n");
      page.append("<p>Type here the Cornipickle properties you want to add.</p>\n");
      page.append("<form method=\"post\" action=\"add\">\n");
      page.append("<div><textarea name=\"properties\"></textarea></div>\n");
      page.append("<input type=\"submit\" />\n");
      page.append("</form>\n");
      page.append("</div>\n");
      page.append("<hr />\n");
      Date d = new Date();
      page.append(d);
      page.append("</body>\n</html>\n");
      String page_string = page.toString();
      // Disable caching on the client
      Headers h = t.getResponseHeaders();
      h.add("Pragma", "no-cache");
      h.add("Cache-Control", "no-cache, no-store, must-revalidate");
      h.add("Expires", "0"); 
      sendResponse(t, HTTP_OK, page_string);
      return true;
    }
  }	

  /**
   * Escapes a string for JavaScript
   * @param s The string
   * @return
   */
  protected static String escapeString(String s)
  {
    s = s.replaceAll("\"", "\\\\\"");
    s = s.replaceAll("\n", "\\\\n");
    s = s.replaceAll("\r", "\\\\r");
    return s;
  }

  /**
   * Sets up the options for the command line parser
   * @return The options
   */
  @SuppressWarnings("static-access")
  private static Options setupOptions()
  {
    Options options = new Options();
    Option opt;
    opt = OptionBuilder
        .withLongOpt("help")
        .withDescription(
            "Display command line usage")
            .create("h");
    options.addOption(opt);
    opt = OptionBuilder
        .withLongOpt("format")
        .withArgName("x")
        .hasArg()
        .withDescription(
            "Output parse tree in format x (dot, xml, txt or json). Default: xml")
            .create("f");
    options.addOption(opt);
    opt = OptionBuilder
        .withLongOpt("verbosity")
        .withArgName("x")
        .hasArg()
        .withDescription(
            "Verbose messages with level x")
            .create();
    options.addOption(opt);
    return options;
  }
  /**
   * Show the benchmark's usage
   * @param options The options created for the command line parser
   */
  private static void showUsage(Options options)
  {
    HelpFormatter hf = new HelpFormatter();
    hf.printHelp("java -jar BullwinkleParser.jar [options] grammar [inputfile]", options);
  }
  /**
   * Sets up the command line parser
   * @param args The command line arguments passed to the class' {@link main}
   * method
   * @param options The command line options to be used by the parser
   * @return The object that parsed the command line parameters
   */
  private static CommandLine setupCommandLine(String[] args, Options options)
  {
    CommandLineParser parser = new PosixParser();
    CommandLine c_line = null;
    try
    {
      // parse the command line arguments
      c_line = parser.parse(options, args);
    }
    catch (org.apache.commons.cli.ParseException exp)
    {
      // oops, something went wrong
      System.err.println("ERROR: " + exp.getMessage() + "\n");
      //HelpFormatter hf = new HelpFormatter();
      //hf.printHelp(t_gen.getAppName() + " [options]", options);
      System.exit(ERR_ARGUMENTS);
    }
    return c_line;
  }
  
  private static void showHeader(PrintStream out)
  {
    out.println("Cornipickle, a web oracle");
    out.println("Version " + VERSION_STRING + ", build " + BUILD_STRING);
  }
}

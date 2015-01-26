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
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.httpserver.InnerFileServer;
import ca.uqac.lif.util.FileReadWrite;

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

  public CornipickleServer()
  {
    super();
    // Instantiate Cornipickle interpreter
    m_interpreter = new Interpreter();
    // Update class reference
    m_referenceClass = this.getClass();
    // Register callbacks
    registerCallback(0, new PropertyAddCallback(this));
    registerCallback(0, new StatusPageCallback(this));
    registerCallback(0, new ProbeCallback(this));
    registerCallback(0, new DummyImageCallback(this));
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

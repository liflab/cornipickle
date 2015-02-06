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
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.Set;

import ro.isdc.wro.model.resource.processor.impl.js.JSMinProcessor;

import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.httpserver.RequestCallback;
import ca.uqac.lif.httpserver.Server;

import com.sun.net.httpserver.HttpExchange;

class ProbeCallback extends RequestCallback<CornipickleServer>
{
  /**
   * Whether to minify the probe's code before sending it to the
   * server. Defaults to false in development mode.
   */
  protected boolean m_minifyJavaScript = false;
  
  /**
   * The processor used to minify the JavaScript code
   */
  protected static JSMinProcessor s_jsMinProcessor;

  public ProbeCallback(CornipickleServer s)
  {
    super(s);
    s_jsMinProcessor = new JSMinProcessor();
  }

  public ProbeCallback(CornipickleServer s, boolean minify)
  {
    this(s);
    m_minifyJavaScript = minify;
  }

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
    String probe_code = generateProbeCode();
    m_server.sendResponse(t, Server.HTTP_OK, probe_code, "application/javascript");
    return true;
  }
  
  protected String generateProbeCode()
  {
    String probe_code = null;
    try
    {
      String witness_code = PackageFileReader.readPackageFile(m_server.getResourceAsStream(m_server.getResourceFolderName() + "/witness.inc.html"));
      probe_code = PackageFileReader.readPackageFile(m_server.getResourceAsStream(m_server.getResourceFolderName() + "/probe.inc.js"));
      probe_code = probe_code.replace("%%WITNESS_CODE%%", escapeString(witness_code));
      probe_code = probe_code.replace("%%SERVER_NAME%%", m_server.getServerName() + ":" + m_server.getServerPort());
      // Add attributes to include
      Set<String> attributes = m_server.m_interpreter.getAttributes();
      StringBuilder attribute_string = new StringBuilder();
      for (String att : attributes)
      {
        attribute_string.append("\"").append(att).append("\",");
      }
      probe_code = probe_code.replace("/*%%ATTRIBUTE_LIST%%*/", attribute_string.toString());
      Set<String> tags = m_server.m_interpreter.getTagNames();
      StringBuilder tag_string = new StringBuilder();
      for (String tag : tags)
      {
        tag_string.append("\"").append(tag).append("\",");
      }
      probe_code = probe_code.replace("/*%%TAG_LIST%%*/", tag_string.toString());
      if (m_minifyJavaScript)
      {
        probe_code = minifyJs(probe_code);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return probe_code;    
  }
  
  protected static String minifyJs(String code) throws IOException
  {
    Reader reader = new StringReader(code);
    Writer writer = new StringWriter();
    s_jsMinProcessor.process(reader, writer);
    return writer.toString();    
  }
  
  /**
   * Escapes a string for JavaScript
   * @param s The string
   * @return The escaped String
   */
  protected static String escapeString(String s)
  {
    s = s.replaceAll("\"", "\\\\\"");
    s = s.replaceAll("\n", "\\\\n");
    s = s.replaceAll("\r", "\\\\r");
    return s;
  }
}

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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.httpserver.RequestCallback;
import ca.uqac.lif.httpserver.Server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

class PropertyAddCallback extends RequestCallback<CornipickleServer>
{
  public PropertyAddCallback(CornipickleServer s)
  {
    super(s);
  }

  @Override
  public boolean fire(HttpExchange t)
  {
    URI u = t.getRequestURI();
    String path = u.getPath();
    String method = t.getRequestMethod();
    return (method.compareToIgnoreCase("post") == 0 || method.compareToIgnoreCase("put") == 0) 
        && path.compareTo("/add") == 0;
  }

  @Override
  public boolean process(HttpExchange t)
  {
    String method = t.getRequestMethod();
    StringBuilder page = new StringBuilder();
    
    // Disable caching on the client
    Headers h = t.getResponseHeaders();
    h.add("Pragma", "no-cache");
    h.add("Cache-Control", "no-cache, no-store, must-revalidate");
    h.add("Expires", "0"); 

    // Read POST data
    InputStream is_post = t.getRequestBody();
    String post_data = Server.streamToString(is_post);

    // Try to decode and parse it
    boolean success = true;
    try
    {
      post_data = URLDecoder.decode(post_data, "UTF-8");
      Map<String,String> params = Server.queryToMap(post_data);
      if (method.compareToIgnoreCase("put") == 0)
      {
        // First, reset the interpreter
        m_server.m_interpreter = new Interpreter();
      }
      String props = params.get("properties");
      if (props == null)
      {
        // Try to get POST payload instead
        props = params.get("");
      }
      if (props != null)
      {
        m_server.m_interpreter.parseProperties(props);
      }
    } 
    catch (ParseException e)
    {
      e.printStackTrace();
      success = false;
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
      success = false;
    }
    if (!success)
    {
      // Baaad request
      m_server.sendResponse(t, Server.HTTP_BAD_REQUEST);
      return true;
    }
    // It worked; obtain new attributes and tag names for the probe
    Set<String> attribute_set = m_server.m_interpreter.getAttributes();
    Set<String> tagname_set = m_server.m_interpreter.getTagNames();
    page.append("{\n \"attributes\" : ");
    page.append(JsonList.toJsonString(attribute_set));
    page.append(",\n \"tagnames\" : ");
    page.append(JsonList.toJsonString(tagname_set));
    page.append("\n}");
    m_server.sendResponse(t, Server.HTTP_OK, page.toString(), "application/json");
    return true;
  }    
}
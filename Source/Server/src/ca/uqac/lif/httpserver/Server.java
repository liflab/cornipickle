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
package ca.uqac.lif.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server implements HttpHandler
{
  /**
   * Common HTTP response codes
   */
  public static final int HTTP_OK = 200;
  public static final int HTTP_NOT_MODIFIED = 304;
  public static final int HTTP_BAD_REQUEST = 400;
  public static final int HTTP_NOT_FOUND = 404;

  /**
   * User-agent string
   */
  protected String m_userAgent = "Server";

  /**
   * Server name, either an IP address or a domain name
   */
  protected String m_serverName = "localhost";
  
  /**
   * Port number
   */
  protected int m_port = 80;

  protected Vector<RequestCallback<? extends Server>> m_callbacks;

  HttpServer m_server;

  public Server()
  {
    super();
    m_callbacks = new Vector<RequestCallback<? extends Server>>();
  }

  public void startServer()
  {
    try
    {
      m_server = HttpServer.create(new InetSocketAddress(m_port), 0);
      m_server.createContext("/", this);
      m_server.setExecutor(null); // creates a default executor
      m_server.start();
    }
    catch (IOException e)
    {
      System.err.println("ERROR: cannot instantiate REST interface on port " + m_port);
      e.printStackTrace();
    } 
  }

  public void stopServer()
  {
    m_server.stop(HTTP_OK);
  }

  public void setServerName(String name)
  {
    m_serverName = name;
  }

  public String getServerName()
  {
    return m_serverName;
  }
  
  public int getServerPort()
  {
    return m_port;
  }
  
  public void setServerPort(int port)
  {
    m_port = port;
  }

  public void setUserAgent(String ua)
  {
    m_userAgent = ua;
  }

  public void registerCallback(int index, RequestCallback<? extends Server> cb)
  {
    m_callbacks.add(index, cb);
  }

  @Override
  public void handle(HttpExchange t) throws IOException
  {
    // Go through registered callbacks
    boolean has_fired = false;
    for (RequestCallback<? extends Server> cb : m_callbacks)
    {
      if (cb.fire(t))
      {
        has_fired = cb.process(t);
        if (has_fired)
        {
          System.out.println(t.getRequestURI().getPath());
          break;
        }
      }
    }
    // No callback was triggered: bad request
    if (!has_fired)
    {
      sendResponse(t, HTTP_BAD_REQUEST);
    }
  }
  
  public void addResponseCookie(HttpExchange t, Cookie c)
  {
    Headers h = t.getResponseHeaders();
    h.add("Set-Cookie", c.getName() + "=" + c.getValue());
  }
  
  public void addResponseCookies(HttpExchange t, Collection<Cookie> cookies)
  {
    for (Cookie c : cookies)
    {
      addResponseCookie(t, c);
    }
  }

  public void sendResponse(HttpExchange t, int response_code)
  {
    sendResponse(t, response_code, "");
  }

  public void sendResponse(HttpExchange t, int response_code, String contents)
  {
    sendResponse(t, response_code, contents.getBytes());
  }

  public void sendResponse(HttpExchange t, int response_code, String contents, String content_type)
  {
    sendResponse(t, response_code, contents.getBytes(), content_type);
  }

  public void sendResponse(HttpExchange t, int response_code, byte[] contents)
  {
    sendResponse(t, response_code, contents, "");
  }

  public void sendResponse(HttpExchange t, int response_code, byte[] contents, String content_type)
  {
    Headers h = t.getResponseHeaders();
    h.add("User-agent", m_userAgent);
    if (content_type != null && !content_type.isEmpty())
    {
      h.add("Content-Type", content_type);
    }
    try
    {
      if (contents == null)
      {
        t.sendResponseHeaders(response_code, 0);	
      }
      else
      {
        t.sendResponseHeaders(response_code, contents.length);
        if (contents.length > 0)
        {
          OutputStream os = t.getResponseBody();
          os.write(contents);
          os.close();
        }
      }
    } 
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Convenience method to transform a GET query into a map of
   * attribute-value pairs. For example, given an URI object
   * representing the URL "http://abc.com/xyz?a=1&b=2", the method
   * will return an object mapping "a" to "1" and "b" to "2".
   * @param u The URI to process
   * @return A map of attribute-value pairs
   */
  public static Map<String,String> uriToMap(URI u)
  {
    String query = u.getQuery();
    return queryToMap(query);
  }

  public static Map<String,String> queryToMap(String query)
  {
    Map<String,String> out = new HashMap<String,String>();
    if (query == null)
      return out;
    String[] pairs = query.split("&");
    if (pairs.length == 1)
    {
      // No params; likely a POST request with payload
      out.put("", pairs[0]);
    }
    else
    {
      // List of attribute/value pairs
      for (String pair : pairs)
      {
        String[] av = pair.split("=");
        String att = av[0];
        String val;
        if (av.length >= 1)
          val = av[1];
        else
          val = "";
        out.put(att, val);
      }
    }
    return out;    
  }

  /**
   * Convenience method to put the contents of an InputStream into
   * a String object. As one can see from the code, this is one
   * prime example of Java verbosity!
   * @param is The InputStream to read from
   * @return The stream's contents; if any error (e.g. I/O) occurs, the
   * method returns an empty string.
   */
  public static String streamToString(InputStream is)
  {
    final String CRLF = System.getProperty("line.separator");
    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();
    String line;
    try
    {
      br = new BufferedReader(new InputStreamReader(is));
      while ((line = br.readLine()) != null)
      {
        sb.append(line).append(CRLF);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (br != null)
      {
        try
        {
          br.close();
        } catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  } 
}

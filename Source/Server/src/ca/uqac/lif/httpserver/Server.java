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
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
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

  /**
   * The list of callbacks to answer HTTP requests
   */
  protected Vector<RequestCallback> m_callbacks;

  /**
   * The underlying Java HTTP server
   */
  HttpServer m_server;
  
  /**
   * The debug mode provides additional verbosity
   */
  protected boolean m_debugMode;

  /**
   * Instantiates an empty server
   */
  public Server()
  {
    super();
    m_callbacks = new Vector<RequestCallback>();
    m_debugMode = false;
  }
  
  /**
   * Sets the debug mode for the server
   * @param b Set to true to activate debug mode, false otherwise
   */
  public void setDebugMode(boolean b)
  {
	  m_debugMode = b;
  }

  /**
   * Starts the server
   */
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

  /**
   * Stops the server
   */
  public void stopServer()
  {
    m_server.stop(CallbackResponse.HTTP_OK);
  }

  /**
   * Sets the server's name. This is either an IP address or a string
   * like "localhost"
   * @param name The name
   */
  public void setServerName(String name)
  {
    m_serverName = name;
  }

  /**
   * Retrieves the server's current name
   * @return The name
   */
  public String getServerName()
  {
    return m_serverName;
  }
  
  /**
   * Retrieves the TCP port number the server is currently
   * listening to
   * @return The port number
   */
  public int getServerPort()
  {
    return m_port;
  }

  /**
   * Sets the TCP port number the server will listen to
   * @param port The port number
   */
  public void setServerPort(int port)
  {
    m_port = port;
  }

  /**
   * Sets the server's name. This name will be used as the value for
   * parameter "User-Agent" in every HTTP response sent.
   * @param ua The name
   */
  public void setUserAgent(String ua)
  {
    m_userAgent = ua;
  }

  /**
   * Adds a new callback to the list of callbacks handled by
   * the server.
   * @param index The position in the list where to insert the callback.
   *   If this value is negative, the insertion position is relative
   *   to the end of the list. For example, a value of -1 will put the element
   *   at the next-to-last position. (To put it at the end, use
   *   {@link #registerCallback(RequestCallback)} without a position.
   * @param cb The callback to add
   */
  public void registerCallback(int index, RequestCallback cb)
  {
  	if (index < 0)
  	{
  		// The position is relative to the *end* of the list
  		m_callbacks.add(index + m_callbacks.size() - 1, cb);
  	}
  	else
  	{
  		m_callbacks.add(index, cb);
  	}
  }
  
  /**
   * Adds a new callback to the list of callbacks handled by
   * the server. The callback is added at the end of the current list.
   * @param cb The callback to add
   */
  public void registerCallback(RequestCallback cb)
  {
    m_callbacks.add(cb);
  }

  @Override
  public void handle(HttpExchange t) throws IOException
  {
    // Go through registered callbacks
    CallbackResponse cbr = null;
    for (RequestCallback cb : m_callbacks)
    {
      if (cb.fire(t))
      {
      	cbr = cb.process(t);
        if (cbr != null)
        {
        	if (m_debugMode)
        		System.out.println(t.getRequestURI().getPath());
        	break;
        }
      }
    }
    if (cbr != null)
    {
    	sendResponse(cbr);
    }
    else
    {
      // No callback was triggered: bad request
    	cbr = new CallbackResponse(t, CallbackResponse.HTTP_BAD_REQUEST, "", "");
      sendResponse(cbr);
    }
  }
  
  public void sendResponse(CallbackResponse cbr)
  {
  	HttpExchange t = cbr.getExchange();
    Headers h = t.getResponseHeaders();
    h.add("User-agent", m_userAgent);
    Map<String,String> headers = cbr.getHeaders();
    for (String name : headers.keySet())
    {
    	h.add(name, headers.get(name));
    }
    byte[] contents = cbr.getContents();
    int response_code = cbr.getCode();
    try
    {
      if (contents == null || contents.length == 0)
      {
        t.sendResponseHeaders(response_code, 0);
        OutputStream os = t.getResponseBody();
        os.close();
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

  /**
   * Convenience method to transform a GET query into a map of
   * attribute-value pairs. For example, given an URI object
   * representing the URL "http://abc.com/xyz?a=1&b=2", the method
   * will return an object mapping "a" to "1" and "b" to "2".
   * @param query The URI to process
   * @return A map of attribute-value pairs
   */
  public static Map<String,String> queryToMap(String query)
  {
    Map<String,String> out = new HashMap<String,String>();
    if (query == null)
      return out;
    String[] pairs = query.split("&");
    if (pairs.length == 1 && pairs[0].indexOf("=") < 0)
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
  
  /**
   * Encodes a string in an URL-encoded form. This is a wrapper method around
   * Java's {@link URLEncoder#encode(String,String)} method, which deals with the encoding
   * and possible exception.
   * @param s The input string
   * @return The encoded string
   */
  public static String urlEncode(String s)
  {
	  String out = s;
		try 
		{
			out = URLEncoder.encode(s, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			// Do nothing
			// Should never occur anyway
		}
		return out;
  }
}

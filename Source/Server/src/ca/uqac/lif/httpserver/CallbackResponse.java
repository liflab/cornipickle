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

import java.util.Collection;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class CallbackResponse
{
  /**
   * Common HTTP response codes
   */
  public static final int HTTP_OK = 200;
  public static final int HTTP_NOT_MODIFIED = 304;
  public static final int HTTP_BAD_REQUEST = 400;
  public static final int HTTP_NOT_FOUND = 404;
  
  /**
   * The content type of the response
   */
  public static enum ContentType {JSON, TEXT, XML, PNG, JS, HTML};
  
  /**
   * The HTTP exchange containing the response headers
   */
  protected HttpExchange m_exchange;
  
  /**
   * The response code
   */
  protected int m_responseCode;
  
  /**
   * The response content type
   */
  protected String m_contentType;
  
  /**
   * The response contents
   */
  protected byte[] m_contents;
  
  public CallbackResponse(HttpExchange t)
  {
  	this(t, HTTP_OK, "", "");
  }
  
  public CallbackResponse(HttpExchange t, int response_code, String contents, String content_type)
  {
  	this(t, response_code, contents.getBytes(), content_type);
  }
  
  public CallbackResponse(HttpExchange t, int response_code, String contents, ContentType type)
  {
  	this(t, response_code, contents.getBytes(), getContentTypeString(type));
  }
  
  public CallbackResponse(HttpExchange t, int response_code, byte[] contents, String content_type)
  {
  	super();
  	m_exchange = t;
  	m_responseCode = response_code;
  	m_contents = contents;
  	m_contentType = content_type;
  }
  
  /**
   * Get the HTTP exchange
   * @return The exchange
   */
  public HttpExchange getExchange()
  {
  	return m_exchange;
  }
  
  /**
   * Disables the client-side caching in the HTTP response to be sent 
   * @param t The exchange
   */
  public void disableCaching()
  {
    Headers h = m_exchange.getResponseHeaders();
    h.add("Pragma", "no-cache");
    h.add("Cache-Control", "no-cache, no-store, must-revalidate");
    h.add("Expires", "0"); 
  }
  
  /**
   * Sets the HTTP response code
   * @param code The code
   */
  public void setCode(int code)
  {
  	m_responseCode = code;
  }
  
  /**
   * Gets the HTTP response code
   * @return The code
   */
  public int getCode()
  {
  	return m_responseCode;
  }
  
  /**
   * Sets the response contents
   * @param contents A string with the response contents
   */
  public void setContents(String contents)
  {
  	m_contents = contents.getBytes();
  }
  
  /**
   * Sets the response contents
   * @param contents An array of bytes with the response contents
   */
  public void setContents(byte[] contents)
  {
  	m_contents = contents;
  }
  
  /**
   * Gets the response contents
   * @return An array of bytes with the response contents
   */
  public byte[] getContents()
  {
  	return m_contents;
  }
  
  /**
   * Sets the response's content type
   * @param t The content type
   */
  public void setContentType(ContentType t)
  {
  	m_contentType = getContentTypeString(t);
  }
  
  /**
   * Sets the response's content type
   * @param t The content type
   */
  public void setContentType(String mime)
  {
  	m_contentType = mime;
  }
  
  /**
   * Retrieves the response's content type
   * @return The content type
   */
  public String getContentType()
  {
  	return m_contentType;
  }
  
  /**
   * Converts a type into a MIME string
   * @param t The content type
   * @return The corresponding MIME string
   */
  public static String getContentTypeString(ContentType t)
  {
  	String out = "";
  	switch (t)
  	{
  	case HTML:
  		out = "text/html";
  		break;
  	case JSON:
  		out = "application/json";
  		break;
  	case XML:
  		out = "application/xml";
  		break;
  	case TEXT:
  		out = "text/plain";
  		break;
  	case PNG:
  		out = "image/png";
  		break;
  	case JS:
  		out = "application/javascript";
  		break;
  	}
  	return out;
  }
  
  /**
   * Add a cookie to the response
   * @param c The cookie to add
   */
  public void addResponseCookie(Cookie c)
  {
    Headers h = m_exchange.getResponseHeaders();
    h.add("Set-Cookie", c.getName() + "=" + c.getValue());
  }

  /**
   * Add multiple cookies to the response
   * @param cookies The cookies to add
   */
  public void addResponseCookies(HttpExchange t, Collection<Cookie> cookies)
  {
    for (Cookie c : cookies)
    {
      addResponseCookie(c);
    }
  }


}

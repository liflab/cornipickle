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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public abstract class RequestCallback<T extends Server>
{
	/**
	 * The me
	 * @author Sylvain
	 *
	 */
	public static enum Method {GET, POST, PUT, DELETE};
	
  public RequestCallback(T s)
  {
    super();
    m_server = s;
  }
  
  public abstract boolean fire(final HttpExchange t);

  public abstract boolean process(HttpExchange t);

  protected T m_server;
  
  /**
   * Creates a string out of a method
   * @param m The method
   * @return The method in string
   */
  public static final String methodToString(Method m)
  {
  	switch (m)
  	{
  	case GET:
  		return "GET";
  	case POST:
  		return "POST";
  	case PUT:
  		return "PUT";
  	case DELETE:
  		return "DELETE";
  	}
  	return "";
  }
  
  /**
   * Disables the client-side caching in the HTTP response to be sent 
   * @param t
   */
  public static void disableCaching(HttpExchange t)
  {
    // Disable caching on the client
    Headers h = t.getResponseHeaders();
    h.add("Pragma", "no-cache");
    h.add("Cache-Control", "no-cache, no-store, must-revalidate");
    h.add("Expires", "0"); 
  }
}

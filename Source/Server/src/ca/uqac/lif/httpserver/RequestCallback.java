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

import com.sun.net.httpserver.HttpExchange;

public abstract class RequestCallback
{
	/**
	 * The me
	 * @author Sylvain
	 *
	 */
	public static enum Method {GET, POST, PUT, DELETE};
	
	/**
	 * Creates a callback
	 */
  public RequestCallback()
  {
    super();
  }
  
  /**
   * Determines whether the request contained in the argument should
   * be handled by this callback
   * @param t The exchange
   * @return true if this request is for this callback, false if it
   *   should be passed on to another callback
   */
  public abstract boolean fire(final HttpExchange t);

  /**
   * Process an HTTP request and prepare an HTTP response
   * @param t The exchange
   * @return A callback response, or null if nothing can be sent
   */
  public abstract CallbackResponse process(HttpExchange t);
  
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
}

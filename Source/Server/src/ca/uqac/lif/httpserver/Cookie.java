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

/**
 * Representation of an HTTP request/response cookie.
 * {@see http://en.wikipedia.org/wiki/HTTP_cookie}
 */
public class Cookie
{
  /**
   * The cookie's name
   */
  protected final String m_name;
  
  /**
   * The cookie's value
   */
  protected final String m_value;
  
  /**
   * Instantiates a cookie
   * @param name The cookie's name
   * @param value The cookie's value
   */
  public Cookie(String name, String value)
  {
    super();
    m_name = name;
    m_value = value;
  }
  
  /**
   * Retrieves the cookie's name
   * @return The name
   */
  public String getName()
  {
    return m_name;
  }
  
  /**
   * Retrieves the cookie's value
   * @return The value
   */
  public String getValue()
  {
    return m_value;
  }
}

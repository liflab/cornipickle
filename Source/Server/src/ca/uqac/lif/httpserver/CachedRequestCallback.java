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

/**
 * Like a normal RequestCallback, except that it will return an HTTP 304
 * ("Not Modified") response every time except the first time it is
 * called
 * @author sylvain
 *
 */
public abstract class CachedRequestCallback<T extends Server> extends RequestCallback<T>
{
  public CachedRequestCallback(T s)
  {
    super(s);
  }

  /**
   * Whether the page has been already served
   */
  protected boolean m_served = false;

  @Override
  public boolean process(HttpExchange t)
  {
    if (m_served)
    {
      m_server.sendResponse(t, Server.HTTP_NOT_MODIFIED);
    }
    else
    {
      m_served = true;
      return serve(t);
    }
    return true;
  }
  
  public void reset()
  {
    m_served = false;
  }
  
  protected abstract boolean serve(HttpExchange t);

}

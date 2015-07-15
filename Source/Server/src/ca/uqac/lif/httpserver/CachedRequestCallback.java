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

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

/**
 * Like a normal RequestCallback, except that it will return an HTTP 304
 * ("Not Modified") response every time except the first time it is
 * called
 * @author sylvain
 *
 */
public abstract class CachedRequestCallback extends RequestCallback
{
  /**
   * Whether the page has been already served
   */
  protected Set<String> m_served;
  
  /**
   * Whether caching is enabled
   */
  protected boolean m_cachingEnabled;
  
  public CachedRequestCallback()
  {
    super();
    m_served = new HashSet<String>();
  }
  
  public void setEnabled(boolean b)
  {
    m_cachingEnabled = b;
  }

  @Override
  public CallbackResponse process(HttpExchange t)
  {
    URI u = t.getRequestURI();
    String path = u.getPath();
    if (!m_cachingEnabled || !m_served.contains(path))
    {
      m_served.add(path);
      return serve(t);
    }
    CallbackResponse out = new CallbackResponse(t, CallbackResponse.HTTP_NOT_MODIFIED, "", "");
    return out;
  }
  
  public void reset()
  {
    m_served.clear();
  }
  
  protected abstract CallbackResponse serve(HttpExchange t);

}

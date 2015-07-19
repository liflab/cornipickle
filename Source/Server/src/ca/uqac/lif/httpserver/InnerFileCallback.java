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

import ca.uqac.lif.cornipickle.util.PackageFileReader;

import com.sun.net.httpserver.HttpExchange;

public class InnerFileCallback extends CachedRequestCallback
{
	protected String m_path;
	
	protected Class<?> m_context;
	
	/**
	 * Whether or not to send a "404 Not Found" when the resource is
	 * not found.
	 */
	protected boolean m_send404;
	
  public InnerFileCallback(String path, Class<?> context)
  {
    super();
    m_path = path;
    m_context = context;
    m_send404 = true;
  }
  
  /**
   * Sets whether or not to send a "404 Not Found" when the resource is
	 * not found. This is useful when chaining multiple file callbacks into
	 * the same server; one wants the first callback to "give a chance"
	 * to the others of serving the resource if it does not find it by
	 * itself. If set to false, the callback will rather send <tt>null</tt>
	 * when serving an unsuccessful request, thereby letting the server
	 * to try the request on further callbacks.
   * @param b true to send a 404 when a resource is not found, false 
   *   to send null instead
   */
  public void send404(boolean b)
  {
  	m_send404 = b;
  }

  @Override
  public boolean fire(HttpExchange t)
  {
    return true;
  }

  @Override
  public CallbackResponse serve(HttpExchange t)
  {
    URI uri = t.getRequestURI();
    CallbackResponse response = new CallbackResponse(t);
    String path = uri.getPath();
    if (path.contains(".."))
    {
      // We try to move up in the structure, and possibly access
      // resources outside the resource folder: deny it
    	response.setCode(CallbackResponse.HTTP_BAD_REQUEST);
      return response;
    }
    // Get file
    System.err.println("Looking for " + m_path + path + " in context " + m_context);
    byte[] file_contents = PackageFileReader.readPackageFileToBytes(m_context, m_path + path);
    if (file_contents != null)
    {
    	response.setContents(file_contents);
    }
    else
    {
      // Resource not found: send 404
    	if (!m_send404)
    	{
    		// Don't return a 404; send null to indicate failure to the server
    		return null;
    	}
    	else
    	{
    		response.setCode(CallbackResponse.HTTP_NOT_FOUND);
    	}
    }
    return response;
  }
}
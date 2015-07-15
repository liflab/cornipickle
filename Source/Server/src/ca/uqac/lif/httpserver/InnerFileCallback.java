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
	
  public InnerFileCallback(String path, Class<?> context)
  {
    super();
    m_path = path;
    m_context = context;
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
    byte[] file_contents = PackageFileReader.readPackageFileToBytes(m_context, m_path + path);
    if (file_contents != null)
    {
    	response.setContents(file_contents);
    }
    else
    {
      // Resource not found: send 404
    	response.setCode(CallbackResponse.HTTP_NOT_FOUND);
    }
    return response;
  }
}
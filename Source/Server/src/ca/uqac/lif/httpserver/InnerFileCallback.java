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

import java.io.InputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;

public class InnerFileCallback extends CachedRequestCallback<InnerFileServer>
{
  public InnerFileCallback(InnerFileServer s)
  {
    super(s);
  }

  @Override
  public boolean fire(HttpExchange t)
  {
    return true;
  }

  @Override
  public boolean serve(HttpExchange t)
  {
    URI uri = t.getRequestURI();
    int response_code = Server.HTTP_OK;
    String path = uri.getPath();
    if (path.contains(".."))
    {
      // We try to move up in the structure, and possibly access
      // resources outside the resource folder: deny it
      m_server.sendResponse(t, Server.HTTP_BAD_REQUEST);
      return true;
    }
    // Get file
    InputStream is = m_server.getResourceAsStream(m_server.getResourceFolderName() + path);
    if (is != null)
    {
      byte[] file_contents = InnerFileServer.readBytes(is);
      m_server.sendResponse(t, response_code, file_contents);
    }
    else
    {
      // Resource not found: send 404
      m_server.sendResponse(t, Server.HTTP_NOT_FOUND);
    }
    return true;
  }
}
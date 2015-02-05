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
package ca.uqac.lif.cornipickle.server;

import java.net.URI;

import com.sun.net.httpserver.HttpExchange;

import ca.uqac.lif.httpserver.InnerFileCallback;
import ca.uqac.lif.httpserver.Server;

/**
 * Inner file callback that excludes some files following a pattern
 * @author sylvain
 *
 */
public class FileCallback extends InnerFileCallback
{
  public FileCallback(CornipickleServer s)
  {
    super(s);
  }
  
  @Override
  public boolean serve(HttpExchange t)
  {
    URI u = t.getRequestURI();
    String path = u.getPath();
    if (path.endsWith("~"))
    {
      // We don't serve backup files
      m_server.sendResponse(t, Server.HTTP_NOT_FOUND);
      return true;
    }
    return super.serve(t);
  }

}

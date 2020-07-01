/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2020 Sylvain Hallé

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

import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.CallbackResponse.ContentType;
import ca.uqac.lif.jerrydog.InnerFileCallback;

/**
 * Inner file callback that excludes some files following a pattern
 * @author sylvain
 *
 */
public class FileCallback extends InnerFileCallback
{
  public FileCallback(CornipickleServer s)
  {
    super("resource", s.getClass());
  }
  
  @Override
  public CallbackResponse process(HttpExchange t)
  {
    URI u = t.getRequestURI();
    String path = u.getPath();
    if (path.endsWith("~"))
    {
      // We don't serve backup files
    	CallbackResponse cbr = new CallbackResponse(t, CallbackResponse.HTTP_NOT_FOUND, "", "");
      return cbr;
    }
    CallbackResponse cbr = super.process(t);
    if (u.toString().endsWith(".js"))
    {
    	// Hack to make sure the static probe is assigned the proper
    	// content type
    	cbr.setContentType(ContentType.JS);
    }
    return cbr;
  }

}

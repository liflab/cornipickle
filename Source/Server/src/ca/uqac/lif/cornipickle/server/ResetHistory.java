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

import java.util.Date;

import com.sun.net.httpserver.HttpExchange;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.RequestCallback;

/**
 * Resets the interpreter. This wipes any data about past events the
 * interpreter may have received, but does not erase the properties
 * themselves.
 * <ul>
 * <li>Method: <b>GET</b></li>
 * <li>Name: <tt>/reset</tt></li>
 * <li>Input: nothing</li>
 * </li>
 * <li>Response: An empty HTTP 200 OK</li>
 * </ul>
 * 
 * @author Sylvain
 *
 */
public class ResetHistory extends InterpreterCallback
{
  public ResetHistory(Interpreter i)
  {
    super(i, RequestCallback.Method.GET, "/reset");
  }

  @Override
  public CallbackResponse process(HttpExchange t)
  {
    m_interpreter.resetHistory();
    StringBuilder page = new StringBuilder();
    page.append("<!DOCTYPE html>\n");
    page.append("<html>\n");
    page.append("<head>\n");
    page.append("<title>Cornipickle Properties</title>\n");
    page.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
    //page.append("<script src=\"http://code.jquery.com/jquery-1.11.2.min.js\"></script>\n");
    page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"screen.css\" />\n");
    page.append("</head>\n");
    page.append("<body>\n");
    page.append("<h1>Reset history</h1>\n");
    page.append("<p>The properties were successfully reset.</p>\n");
    page.append("<p><a href=\"status\">Go to status page</a></p>\n");
    page.append("<hr />\n");
    Date d = new Date();
    page.append(d);
    page.append("</body>\n</html>\n");
    String page_string = page.toString();
    CallbackResponse out = new CallbackResponse(t, CallbackResponse.HTTP_OK, page_string, CallbackResponse.ContentType.HTML);
    out.disableCaching();
    return out;
  }

}

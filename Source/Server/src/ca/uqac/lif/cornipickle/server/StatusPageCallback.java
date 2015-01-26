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
import java.util.Date;
import java.util.Map;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.httpserver.RequestCallback;
import ca.uqac.lif.httpserver.Server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

class StatusPageCallback extends RequestCallback<CornipickleServer>
{
  public StatusPageCallback(CornipickleServer s)
  {
    super(s);
  }

  @Override
  public boolean fire(HttpExchange t)
  {
    URI u = t.getRequestURI();
    String path = u.getPath();
    return path.compareTo("/status") == 0;
  }

  @Override
  public boolean process(HttpExchange t)
  {
    StringBuilder page = new StringBuilder();
    page.append(pageHead("Cornipickle status"));
    page.append("<h1>Cornipickle Status</h1>");
    Map<StatementMetadata,Interpreter.Verdict> verdicts = m_server.m_interpreter.getVerdicts();
    page.append("<ul class=\"verdicts\">\n");
    for (StatementMetadata key : verdicts.keySet())
    {
      Interpreter.Verdict v = verdicts.get(key);
      if (v == Interpreter.Verdict.TRUE)
      {
        page.append("<li class=\"true\">").append(key.get("name")).append("</li>");
      }
      else if (v == Interpreter.Verdict.FALSE)
      {
        page.append("<li class=\"false\">").append(key.get("name")).append("</li>");
      }
      else
      {
        page.append("<li class=\"inconclusive\">").append(key.get("name")).append("</li>");
      }
    }
    page.append("</ul>\n");
    page.append("\n<div id=\"add-properties\">\n");
    page.append("<h2>Add properties</h2>\n\n");
    page.append("<p>Type here the Cornipickle properties you want to add.</p>\n");
    page.append("<form method=\"post\" action=\"add\">\n");
    page.append("<div><textarea name=\"properties\"></textarea></div>\n");
    page.append("<input type=\"submit\" />\n");
    page.append("</form>\n");
    page.append("</div>\n");
    page.append(pageFoot());
    String page_string = page.toString();
    // Disable caching on the client
    Headers h = t.getResponseHeaders();
    h.add("Pragma", "no-cache");
    h.add("Cache-Control", "no-cache, no-store, must-revalidate");
    h.add("Expires", "0"); 
    m_server.sendResponse(t, Server.HTTP_OK, page_string);
    return true;
  }
  
  protected StringBuilder pageHead(String title)
  {
    StringBuilder page = new StringBuilder();
    page.append("<!DOCTYPE html>\n");
    page.append("<html>\n");
    page.append("<head>\n");
    page.append("<title>").append(title).append("</title>\n");
    page.append("<script src=\"http://code.jquery.com/jquery-1.11.2.min.js\"></script>\n");
    page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"screen.css\" />\n");
    page.append("</head>\n<body>\n");
    return page;
  }
  
  protected StringBuilder pageFoot()
  {
    StringBuilder page = new StringBuilder();
    page.append("<div id=\"footer\">\n");
    page.append("<hr />\n");
    Date d = new Date();
    page.append(d);
    page.append("</div>\n");
    page.append("</body>\n</html>\n");
    return page;
  }
} 
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
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.PredicateDefinition;
import ca.uqac.lif.cornipickle.SetDefinition;
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
    HtmlFormatter formatter = new HtmlFormatter();
    StringBuilder page = new StringBuilder();
    page.append(pageHead("Cornipickle status"));
    page.append("<h1>Cornipickle Status</h1>");
    Date last_contact = m_server.getLastProbeContact();
    if (last_contact == null)
    {
    	page.append("<p id=\"last-probe-contact\">No contact with probe so far.</p>\n");
    }
    else
    {
    	page.append("<p id=\"last-probe-contact\">Last contact with probe on ");
    	page.append(m_server.getLastProbeContact()).append("</p>\n");
    }
    Map<StatementMetadata,Interpreter.Verdict> verdicts = m_server.m_interpreter.getVerdicts();
    int num_errors = 0;
    StringBuilder verdict_string = new StringBuilder();
    verdict_string.append("<ul class=\"verdicts\">\n");
    for (StatementMetadata key : verdicts.keySet())
    {
      Interpreter.Verdict v = verdicts.get(key);
      String class_name = "inconclusive";
      if (v == Interpreter.Verdict.TRUE)
      {
      	class_name = "true";
      }
      else if (v == Interpreter.Verdict.FALSE)
      {
        class_name = "false";
        num_errors++;
      }
      verdict_string.append("<li class=\"").append(class_name).append("\">");
      verdict_string.append("<span class=\"prop-name\">").append(key.get("name")).append("</span>\n");
      verdict_string.append("<div class=\"property-contents\">\n");
      verdict_string.append(formatter.getFormatted(m_server.m_interpreter.getProperty(key)));
      verdict_string.append("</div>\n");
      verdict_string.append("</li>");
    }
    verdict_string.append("</ul>\n");
    if (num_errors == 0)
    {
    	if (verdicts.isEmpty())
    	{
    		page.append("<p class=\"verdicts-empty\">Cornipickle has no property to evaluate.</p>");
    	}
    	else
    	{
    		page.append("<p class=\"verdicts-no-error\">All's well! All properties evaluate to true.</p>");	
    	}
    }
    else if (num_errors == 1)
    {
    	page.append("<p class=\"verdicts-errors\">Oops! There is a problem with some property.</p>");
    }
    else
    {
    	page.append("<p class=\"verdicts-errors\">Oops! There is a problem with ").append(num_errors).append(" properties.</p>");
    }
    page.append("<h2>Properties</h2>\n");
    page.append(verdict_string);
    // Show predicates
    page.append("<h2>Predicates</h2>\n");
    page.append("<ul class=\"predicates\">\n");
    List<PredicateDefinition> preds = m_server.m_interpreter.getPredicates();
    for (PredicateDefinition pd : preds)
    {
    	page.append("<li>").append(formatter.getFormatted(pd)).append("</li>\n");
    }
    page.append("</ul>");
    // Show sets
    page.append("<h2>Sets</h2>\n");
    page.append("<ul class=\"sets\">\n");
    List<SetDefinition> sets = m_server.m_interpreter.getSetDefinitions();
    for (SetDefinition sd : sets)
    {
    	page.append("<li>").append(formatter.getFormatted(sd)).append("</li>\n");
    }
    page.append("</ul>");
    page.append("\n<div id=\"add-properties\">\n");
    page.append("<h2>Add properties</h2>\n\n");
    page.append("<p>Type here the Cornipickle properties you want to add.</p>\n");
    page.append("<form method=\"post\" action=\"add\">\n");
    page.append("<div><textarea name=\"properties\" rows=\"10\" cols=\"40\"></textarea></div>\n");
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
    m_server.sendResponse(t, Server.HTTP_OK, page_string, "text/html");
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
    page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"colouring.css\" />\n");
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
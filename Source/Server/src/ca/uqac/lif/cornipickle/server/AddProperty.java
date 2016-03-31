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

import java.util.Map;
import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.jerrydog.CallbackResponse;

import com.sun.net.httpserver.HttpExchange;

/**
 * Adds a set of properties to the interpreter.
 * <ul>
 * <li>Method: <b>PUT</b></li>
 * <li>Name: <tt>/add</tt></li>
 * <li>Input:
 * <ul>
 * <li>An unnamed string containing valid Cornipickle statements</li>
 * </ul>
 * </li>
 * <li>Response: JSON
 * <ul>
 * <li>A JSON structure of the form:
 * <pre>
 * {
 *   "tagnames"   : ["tag1", "tag2", ...],
 *   "attributes" : ["att1", "att2", ...]
 * }
 * </pre>
 * where <tt>tagnames</tt> is a list of tagnames (e.g. "a", "p", "h1") that
 * must be observed by the probe, and 
 * <tt>attributes</tt> is a list of strings representing the attributes of
 * these elements that must be reported by the probe (e.g. "width", "top", etc.).
 * </li>
 * </ul>
 * </li>
 * </ul>
 * @author Sylvain
 *
 */
class AddProperty extends InterpreterCallback
{
  public AddProperty(Interpreter i)
  {
    super(i, Method.PUT, "/add");
  }

  @Override
  public CallbackResponse process(HttpExchange t)
  {
  	CallbackResponse response = new CallbackResponse(t);

    // Disable caching on the client
    response.disableCaching();

    // Read request parameters
    Map<String,String> params = getParameters(t);
    
    // Try to decode and parse it
    boolean success = true;
    String props = params.get("");
    
    //Decode the string (had to be added because of the probe server's encoding)
    try {
		props = URLDecoder.decode(props,"UTF-8");
	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
    try
    {
      if (props != null)
      {
      	// Wipe the status of the interpreter first
      	m_interpreter.clear();
      	m_interpreter.parseProperties(props);
      }
    } 
    catch (ParseException e)
    {
      e.printStackTrace();
      success = false;
    }
    if (!success)
    {
      // Baaad request
    	response.setCode(CallbackResponse.HTTP_BAD_REQUEST);
      return response;
    }
    // It worked; obtain new attributes and tag names for the probe
    JsonMap output = new JsonMap();
    Set<String> attribute_set = m_interpreter.getAttributes();
    JsonList attributes = new JsonList();
    for (String att : attribute_set)
    {
    	attributes.add(att);
    }
    output.put("attributes", attributes);
    Set<String> tagname_set = m_interpreter.getTagNames();
    JsonList tagnames = new JsonList();
    for (String att : tagname_set)
    {
    	tagnames.add(att);
    }
    output.put("tagnames", tagnames);    
    response.setContents(output.toString());
    response.setContentType(CallbackResponse.ContentType.JSON);
    return response;
  }    
}
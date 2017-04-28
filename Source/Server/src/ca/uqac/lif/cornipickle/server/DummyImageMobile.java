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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.InnerFileServer;
import ca.uqac.lif.jerrydog.RequestCallback;

import com.sun.net.httpserver.HttpExchange;

/**
 * Returns a dummy one-pixel image. This request is used as a means of
 * communication between the JavaScript probe and the interpreter. The
 * request passes as GET parameters an URL-encoded JSON object containing
 * information about the current state of the page. The response is made
 * of a one-pixel PNG image, and a cookie containing a JSON object. This
 * object is retrieved by the JS probe, which uses it to determine whether
 * any elements of the page should be highlighted.
 * <ul>
 * <li>Method: <b>GET</b></li>
 * <li>Name: <tt>/image</tt></li>
 * <li>Input:
 * <ul>
 * <li>A parameter named <tt>contents</tt>, which contains a urlencoded
 * JSON string of the following form:
 * <pre>
 * {
 *   TODO
 * }
 * </pre>
 * </li>
 * </ul>
 * </li>
 * <li>Response: 
 * <ul>
 * <li>Binary data for a one-pixel PNG image</li>
 * <li>Into a cookie named <tt>cornipickle</tt>, a JSON string of the form:
 * <pre>
 * {
 *   "num-true"           : 0,
 *   "num-false"          : 0,
 *   "num-inconclusive"   : 0,
 *   "global-verdict"     : "TRUE",
 *   "highlight-ids"     : [
 *      {
 *        "ids"     : [[0, 1, 2, ...], [3, 4], ...],
 *        "caption" : "Some text"
 *      },
 *      ...   
 *   ]
 * }
 * </pre>
 * The attributes <tt>num-true</tt>, <tt>num-false</tt> and
 * <tt>num-inconclusive</tt> give the number of Cornipickle statements that
 * evaluate to true, false and inconclusive respectively. The attribute
 * <tt>global-verdict</tt> is either "TRUE", "FALSE" or "INCONCLUSIVE",
 * depending on the verdict of each statement.
 * <p>
 * The property <tt>highlight-ids</tt> is a list of structures, each correpsonding
 * to one Cornipickle statement, and having two attributes. Attribute
 * <tt>ids</tt> is a list, each element of which is a list of ids. These
 * represents the groups of elements that could be highlighted in the browser
 * to signal an error. For example, the group <tt>[0, 1, 2, ...]</tt> is
 * a first group of element IDs involved in one error instance of the property.
 * The group <tt>[3, 4]</tt> represents a second group of element IDs involved
 * in another error instance of the same property. Finally, the attribute
 * <tt>caption</tt> corresponds to the metadata <tt>@caption</tt> associated with
 * the property, if any. Note that properties that are not violated will not
 * create such a structure in the response cookie.
 * </li>
 * </ul>
 * </li>
 * </ul>
 * @author Sylvain
 *
 */
class DummyImageMobile extends InterpreterCallback
{

  
  static JsonParser s_jsonParser;

  public DummyImageMobile(Interpreter i)
  {
    super(i, RequestCallback.Method.POST, "/imageMobile/");
    s_jsonParser = new JsonParser();
  }

  @SuppressWarnings("deprecation")
@Override
  public CallbackResponse process(HttpExchange t)
  {
    Map<String,String> attributes = getParameters(t);
    
    JsonElement j = null;
    try {
   j = s_jsonParser.parse(URLDecoder.decode(attributes.get("contents").toString(), "UTF-8"));
  	//j = s_jsonParser.parse(attributes.toString().substring(2,attributes.toString().length()-1 ));
      if(attributes.get("interpreter") != null)
      {
    	 // System.out.println("nombre: "+URLDecoder.decode(attributes.get("interpreter"), "UTF-8").length());
    	  m_interpreter = m_interpreter.restoreFromMemento(URLDecoder.decode(attributes.get("interpreter").toString(), "UTF-8"));
    	System.out.println (m_interpreter.toString());
      }
    } catch (JsonParseException e) {
      e.printStackTrace(); //Never supposed to happen....
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (j != null)
    {
      m_interpreter.evaluateAll(j);
      //m_server.setLastProbeContact();
    }    
    // Select the dummy image to send back
    Map<StatementMetadata,Verdict> verdicts = m_interpreter.getVerdicts();
    // Create response
    CallbackResponse cbr = new CallbackResponse(t);
    cbr.setHeader("Access-Control-Allow-Origin", "*");
    cbr.setContents(createResponseBody(verdicts, m_interpreter.saveToMemento(), null));
    cbr.setContentType(CallbackResponse.ContentType.JSON);
    m_interpreter.clear(); 

    return cbr;
  }


  /**
   * Creates the response body out of the verdict of each property handled by the
   * interpreter. This response contains, for each property, its caption,
   * a list of element IDs to highlight (if any), as well as the total
   * number of true/false/inconclusive properties.
   * In addition, it contains the serialized state of the current interpreter
   * @param verdicts A map from the metadata for each property to
   *   its current verdict
   * @param interpreter String in base 64 representing the state of the interpreter
   * @param image The image to return as an array of bytes
   * @return A JSON string to be passed as the response to
   *   the browser
   */
  protected static String createResponseBody(Map<StatementMetadata,Verdict> verdicts, String interpreter, byte[] image)
  {
    int num_false = 0;
    int num_true = 0;
    int num_inconclusive = 0;
    Verdict outcome = new Verdict(Verdict.Value.TRUE);
    JsonList highlight_ids = new JsonList();
    for (StatementMetadata key : verdicts.keySet())
    {
    	JsonMap element = new JsonMap();
    	JsonList id_to_highlight = new JsonList();
      Verdict v = verdicts.get(key);
      outcome.conjoin(v);
      if (v.is(Verdict.Value.FALSE))
      {
        num_false++;
        id_to_highlight.addAll(getIdsToHighlight(v));
      }
      else if (v.is(Verdict.Value.TRUE))
      {
        num_true++;
      } 
      else
      {
        num_inconclusive++;
      }
      element.put("ids", id_to_highlight);
      element.put("caption", new JsonString(CornipickleServer.escapeQuotes(key.get("description"))));
      if(key.get("link")!=null){
    	  JsonString s=new JsonString(CornipickleServer.escapeQuotes(key.get("link")));
    	  element.put("link", new JsonString(CornipickleServer.escapeQuotes(key.get("link"))));
      }
    	  else {
    		  element.put("link", "");
    		  
    		  
    	   
         }
      highlight_ids.add(element);
    }
    JsonMap result = new JsonMap();
    result.put("global-verdict", outcome.toPlainString());
    result.put("num-true", num_true);
    result.put("num-false", num_false);
    result.put("num-inconclusive", num_inconclusive);
    result.put("highlight-ids", highlight_ids);
    result.put("interpreter", interpreter);
    return result.toString("", true); 
  }
  
  protected static JsonList getIdsToHighlight(Verdict v)
  {
    JsonList ids = new JsonList();
    Set<Set<JsonElement>> tuples = v.getWitness().flatten();
    for (Set<JsonElement> tuple : tuples)
    {
      JsonList out = new JsonList();
      for (JsonElement e : tuple)
      {
        if (!(e instanceof JsonMap))
        {
          continue;
        }
        JsonMap m = (JsonMap) e;
        JsonElement id = m.get("cornipickleid");
        if (id == null || !(id instanceof JsonNumber))
        {
          continue;
        }
        out.add((JsonNumber) id);
      }
      ids.add(out);
    }
    return ids;
  }

}
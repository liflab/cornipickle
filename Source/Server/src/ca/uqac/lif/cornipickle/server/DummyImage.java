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

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonFastParser;
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.json.JsonMap;
import ca.uqac.lif.cornipickle.json.JsonNumber;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.cornipickle.json.JsonString;
import ca.uqac.lif.httpserver.CallbackResponse;
import ca.uqac.lif.httpserver.Cookie;
import ca.uqac.lif.httpserver.InnerFileServer;
import ca.uqac.lif.httpserver.RequestCallback;

import com.sun.net.httpserver.HttpExchange;

class DummyImage extends InterpreterCallback
{
  /**
   * Dummy image made of one white pixel
   */
  static final byte[] s_dummyImage = InnerFileServer.readBytes(
      CornipickleServer.class.getResourceAsStream("resource/dummy-image.png"));
  
  /**
   * Dummy image made of one red pixel
   */
  static final byte[] s_dummyImageRed = InnerFileServer.readBytes(
      CornipickleServer.class.getResourceAsStream("resource/dummy-image-red.png"));
  
  /**
   * Dummy image made of one green pixel
   */  
  static final byte[] s_dummyImageGreen = InnerFileServer.readBytes(
      CornipickleServer.class.getResourceAsStream("resource/dummy-image-green.png"));
  
  static JsonParser s_jsonParser;

  public DummyImage(Interpreter i)
  {
    super(i, RequestCallback.Method.GET, "/image");
    s_jsonParser = new JsonFastParser();
  }

  @Override
  public CallbackResponse process(HttpExchange t)
  {
    Map<String,String> attributes = getParameters(t); 

    // Extract JSON from URL string
    String json_encoded = attributes.get("contents");
    if (json_encoded != null)
    {
      JsonElement j = null;
      // Parse JSON
      try
      {
        String json_decoded = URLDecoder.decode(json_encoded, "UTF-8");
        j = s_jsonParser.parse(json_decoded);
        System.out.println("JSON received");
        System.out.println(json_decoded);
      }
      catch (JsonParseException e)
      {
        e.printStackTrace();
      } 
      catch (UnsupportedEncodingException e)
      {
        e.printStackTrace();
      }
      if (j != null)
      {
        m_interpreter.evaluateAll(j);
        //m_server.setLastProbeContact();
      }
    }
    // Select the dummy image to send back
    Map<StatementMetadata,Verdict> verdicts = m_interpreter.getVerdicts();
    byte[] image_to_return = selectImage(verdicts);
    // Create cookie response
    CallbackResponse cbr = new CallbackResponse(t);
    String cookie_json_string = createResponseCookie(verdicts);
    cbr.addResponseCookie(new Cookie("cornipickle", cookie_json_string));
    cbr.setContents(image_to_return);
    cbr.setContentType(CallbackResponse.ContentType.PNG);
    return cbr;
  }

  /**
   * Determines which of the three images (red, white, blue) to send back
   *   to the browser. The image is chosen as follows:
   *   <ul>
   *   <li>All properties evaluate to true: green</li>
   *   <li>No property to evaluate: white</li>
   *   <li>At least one property evaluates to false: red</li>
   *   </ul>
   * @param verdicts A map from the metadata for each property to
   *   its current verdict
   * @return The byte array with the contents of the selected image
   */
  protected static byte[] selectImage(Map<StatementMetadata,Verdict> verdicts)
  {
    int num_errors = 0;
    byte[] image_out = s_dummyImage;
    for (StatementMetadata key : verdicts.keySet())
    {
      Verdict v = verdicts.get(key);
      if (v.is(Verdict.Value.FALSE))
      {
        num_errors++;
      }
    }
    if (num_errors == 0)
    {
      if (verdicts.isEmpty())
      {
        // Cornipickle has no property to evaluate
      }
      else
      {
        // All's well! All properties evaluate to true 
      }
    }
    else
    {
      // Errors found
      image_out = s_dummyImageRed;
    }
    return image_out;
  }
  
  /**
   * Creates a cookie out of the verdict of each property handled by the
   * interpreter. This cookie contains, for each property, its caption,
   * a list of element IDs to highlight (if any), as well as the total
   * number of true/false/inconclusive properties.
   * @param verdicts A map from the metadata for each property to
   *   its current verdict
   * @return A JSON string to be passed as a cookie in the response to
   *   the browser
   */
  protected static String createResponseCookie(Map<StatementMetadata,Verdict> verdicts)
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
      highlight_ids.add(element);
    }
    JsonMap result = new JsonMap();
    result.put("global-verdict", outcome.toPlainString());
    result.put("num-true", num_true);
    result.put("num-false", num_false);
    result.put("num-inconclusive", num_inconclusive);
    result.put("highlight-ids", highlight_ids);
    // Below, we use true to get a compact JSON string without CF/LF
    // (otherwise the cookie won't be passed correctly to the browser)
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

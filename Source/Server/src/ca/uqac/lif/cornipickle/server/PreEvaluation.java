package ca.uqac.lif.cornipickle.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import com.sun.net.httpserver.HttpExchange;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.InnerFileServer;
import ca.uqac.lif.jerrydog.RequestCallback;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class PreEvaluation extends InterpreterCallback {

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

  public PreEvaluation(Interpreter i)
  {
    super(i, RequestCallback.Method.POST, "/preevaluate/");
    s_jsonParser = new JsonParser();
  }
  
  /**
   * Process like DummyImage but returns the original interpreter instead of the updated one.
   */
  @Override
  public CallbackResponse process(HttpExchange t)
  {
    Map<String,String> attributes = getParameters(t);
    
    JsonElement j = null;
    String memento = "";
    try {
      j = s_jsonParser.parse(URLDecoder.decode(attributes.get("contents"), "UTF-8"));
      if(attributes.get("interpreter") != null)
      {
        memento = URLDecoder.decode(attributes.get("interpreter"), "UTF-8");
        m_interpreter = m_interpreter.restoreFromMemento(memento);
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
    byte[] image_to_return = selectImage(verdicts);
    // Create response
    CallbackResponse cbr = new CallbackResponse(t);
    cbr.setHeader("Access-Control-Allow-Origin", "*");
    cbr.setContents(createResponseBody(verdicts, memento, image_to_return));
    cbr.setContentType(CallbackResponse.ContentType.JSON);
    m_interpreter.clear();
    // DEBUG: print state
    //com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder();
    //com.google.gson.Gson gson = builder.create();
    //System.out.println(gson.toJson(m_interpreter));
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
      highlight_ids.add(element);
    }
    JsonMap result = new JsonMap();
    result.put("global-verdict", outcome.toPlainString());
    result.put("num-true", num_true);
    result.put("num-false", num_false);
    result.put("num-inconclusive", num_inconclusive);
    result.put("highlight-ids", highlight_ids);
    result.put("interpreter", interpreter);
    result.put("image", "data:image/png;base64," + DatatypeConverter.printBase64Binary(image));
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

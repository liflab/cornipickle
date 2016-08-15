package ca.uqac.lif.cornipickle.fiddle;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;

/**
 * Fiddle operation to parse the language to see if there's any error
 * <p>
 * The format of the request is as follows:
 * <pre>
 * {
 *   "action"   : "parse",
 *   "code"     : "..."
 * }
 * </pre>
 * where the content of the <tt>code</tt> is the code to parse.
 * <p>
 * The format of the response is a boolean:
 * {
 *   "isValid": "true" or "false"
 * }
 * 
 * @author francis
 *
 */
public class Parse extends FiddleOperation {

  @Override
  public boolean fire(JsonMap in) {
    String action = ((JsonString) in.get("action")).stringValue();
    return action.compareToIgnoreCase("parse") == 0;
  }

  @Override
  public JsonElement doOperation(JsonMap argument, Interpreter i) {
    String code = ((JsonString)(argument.get("code"))).stringValue();
    i.getParser().setPartialParsing(true);
    boolean isValid = true;
    try {
      isValid = i.getParser().syntaxAnalysis(code);
    } catch (ParseException e) {
      isValid = false;
    }
    JsonMap json = new JsonMap();
    json.put("isValid", isValid);
    return json;
  }
  
}

package ca.uqac.lif.cornipickle.fiddle;

import java.util.List;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.json.JsonList;

/**
 * Fiddle operation to get the list of alternate rules of a rule.
 * <p>
 * The format of the request is as follows:
 * <pre>
 * {
 *   "action"   : "expand",
 *   "rule"     : "&lt;rule&gt;"
 * }
 * </pre>
 * where the content of the <tt>rule</tt> attribute is a rule of
 * the form &lt;rule&gt;.
 * <p>
 * The format of the response is a JSON list:
 * {
 *   "alternatives":
 *      [
 *        "&lt;rule1&gt;",
 *        "&lt;rule2&gt;",
 *        "&lt;rule3&gt;",
 *        ...
 *      ]
 * }
 * 
 * @author francis
 *
 */
public class Expand extends FiddleOperation{

  @Override
  public boolean fire(JsonMap in) {
    String action = ((JsonString) in.get("action")).stringValue();
    return action.compareToIgnoreCase("expand") == 0;
  }

  @Override
  public JsonElement doOperation(JsonMap argument, Interpreter i) {
    List<String> rules = i.getParser().getParser().getAlternatives(
        ((JsonString)(argument.get("rule"))).stringValue());
    JsonMap map = new JsonMap();
    JsonList json = new JsonList();
    for(String tok : rules)
    {
      json.add(new JsonString(tok));
    }
    map.put("alternatives", json);
    return map;
  }

}

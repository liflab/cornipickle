package ca.uqac.lif.cornipickle.fiddle;

import java.util.List;

import ca.uqac.lif.bullwinkle.TokenString;
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
 *   "rule"     : "<rule>"
 * }
 * </pre>
 * where the content of the <tt>rule</tt> attribute is a rule of
 * the form < rule >.
 * <p>
 * The format of the response is a JSON list:
 * {
 *   "alternatives":
 *      [
 *        "<rule1>",
 *        "<rule2>",
 *        "<rule3>",
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
    List<String> rules = i.getParser().m_parser.getAlternatives(
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

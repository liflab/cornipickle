package ca.uqac.lif.cornipickle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ca.uqac.lif.cornipickle.json.JsonElement;

public class PredicateCall extends Statement
{
  protected PredicateDefinition m_predicate;
  
  protected String m_matchedString;
  
  protected Vector<String> m_captureBlocks;
  
  public PredicateCall(PredicateDefinition predicate, String match, List<String> capture_blocks)
  {
    super();
    m_predicate = predicate;
    m_matchedString = match;
    m_captureBlocks = new Vector<String>(capture_blocks);
  }

  @Override
  public boolean evaluate(JsonElement j, Map<String, JsonElement> d)
  {
    Map<String,JsonElement> new_d = new HashMap<String,JsonElement>(d);
    // Put in the map the values
    for (int i = 0; i < m_captureBlocks.size(); i++)
    {
      String var_in_match = m_captureBlocks.get(i);
      String var_in_pred = m_predicate.getCaptureGroup(i);
      new_d.put(var_in_pred, d.get(var_in_match));
    }
    return m_predicate.evaluate(j, new_d);
  }

  @Override
  public String toString(String indent)
  {
    return m_matchedString;
  }

}

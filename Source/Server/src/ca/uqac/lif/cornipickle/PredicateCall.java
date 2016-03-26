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
package ca.uqac.lif.cornipickle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ca.uqac.lif.json.JsonElement;

public class PredicateCall extends Statement
{
  protected PredicateDefinition m_predicate;
  
  protected String m_matchedString;
  
  protected Vector<String> m_captureBlocks;
  
  PredicateCall()
  {
	  super();
  }
  
  public PredicateCall(PredicateDefinition predicate, String match, List<String> capture_blocks)
  {
    super();
    m_predicate = predicate;
    m_matchedString = match;
    m_captureBlocks = new Vector<String>(capture_blocks);
  }
  
  public String getMatchedString()
  {
    return m_matchedString;
  }
  
  public PredicateDefinition getPredicateDefinition()
  {
    return m_predicate;
  }
  
  @Override
  public void resetHistory()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
    m_predicate.resetHistory();
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    Map<String,JsonElement> new_d = new HashMap<String,JsonElement>(d);
    // Put in the map the values
    for (int i = 0; i < m_captureBlocks.size(); i++)
    {
      String var_in_match = m_captureBlocks.get(i);
      String var_in_pred = m_predicate.getCaptureGroup(i);
      new_d.put(var_in_pred, d.get(var_in_match));
    }
    m_verdict = m_predicate.evaluate(j, new_d);
    return m_verdict;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String,JsonElement> d)
  {
    Map<String,JsonElement> new_d = new HashMap<String,JsonElement>(d);
    // Put in the map the values
    for (int i = 0; i < m_captureBlocks.size(); i++)
    {
      String var_in_match = m_captureBlocks.get(i);
      String var_in_pred = m_predicate.getCaptureGroup(i);
      new_d.put(var_in_pred, d.get(var_in_match));
    }
    return m_predicate.evaluateAtemporal(j, new_d);
  }

  @Override
  public String toString(String indent)
  {
    return m_matchedString;
  }

  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    visitor.pop();
  }
  
  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    visitor.pop();
  }
  
  @Override
  public PredicateCall getClone()
  {
    PredicateCall out = new PredicateCall(m_predicate.getClone(), m_matchedString, m_captureBlocks);
    return out;
  }

  @Override
  public boolean isTemporal()
  {
    return m_predicate.isTemporal();
  }
}

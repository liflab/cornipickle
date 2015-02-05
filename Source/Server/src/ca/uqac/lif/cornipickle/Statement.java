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
import java.util.Map;

import ca.uqac.lif.cornipickle.json.JsonElement;

public abstract class Statement extends LanguageElement
{ 
  protected Verdict m_verdict;
  
  public Statement()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
  }

  public final Verdict evaluate(JsonElement j)
  {
    Map<String,JsonElement> d = new HashMap<String,JsonElement>();
    return evaluate(j, d);
  }
  
  /**
   * Evaluates a statement in an <em>atemporal</em> way
   * @param j The page snapshot to use for the evaluation
   * @return An evaluation verdict. Note that atemporal evaluation
   *   is never supposed to return INCONCLUSIVE.
   */
  public final Verdict evaluateAtemporal(JsonElement j)
  {
    Map<String,JsonElement> d = new HashMap<String,JsonElement>();
    return evaluateAtemporal(j, d);
  }

  public final Verdict evaluate(JsonElement j, Map<String,JsonElement> d)
  {
    if (!m_verdict.is(Verdict.Value.INCONCLUSIVE)) //&& isTemporal())
    {
      return m_verdict;
    }
    return evaluateTemporal(j, d);
  }
  
  public abstract Verdict evaluateAtemporal(JsonElement j, Map<String,JsonElement> d);
  
  /**
   * Checks whether a statement is "temporal". It is so when
   * the statement contains at least one temporal operator.
   * @return Temporal or not
   */
  public abstract boolean isTemporal();
  
  public abstract Verdict evaluateTemporal(JsonElement j, Map<String,JsonElement> d);
  
  //public abstract Verdict evaluateATemporal(JsonElement j, Map<String,JsonElement> d);

  public abstract Statement getClone();

  public abstract void resetHistory();


}

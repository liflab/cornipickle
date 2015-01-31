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

import java.util.Map;

import ca.uqac.lif.cornipickle.json.JsonElement;

public class ImpliesStatement extends AndStatement
{
  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    if (m_statements.size() < 2)
    {
      m_verdict = Verdict.FALSE;
      return m_verdict;
    }
    Statement left = m_statements.get(0);
    Statement right = m_statements.get(1);
    Verdict v_left = left.evaluate(j, d);
    Verdict v_right = right.evaluate(j, d);
    // p -> q == !p or q
    m_verdict = threeValuedOr(threeValuedNot(v_left), v_right);
    return m_verdict;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
  {
    if (m_statements.size() < 2)
    {
      m_verdict = Verdict.FALSE;
      return m_verdict;
    }
    Statement left = m_statements.get(0);
    Statement right = m_statements.get(1);
    Verdict v_left = left.evaluateAtemporal(j, d);
    Verdict v_right = right.evaluateAtemporal(j, d);
    // p -> q == !p or q
    return threeValuedOr(threeValuedNot(v_left), v_right);
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("If (\n");
    out.append(m_statements.get(0).toString(indent + "  "));
    out.append(")\n").append(indent).append("Then (\n");
    out.append(m_statements.get(1).toString(indent + "  "));
    out.append(")");
    return out.toString();
  }
  
  @Override
  public ImpliesStatement getClone()
  {
    ImpliesStatement out = new ImpliesStatement();
    for (Statement s : m_statements)
    {
      out.addOperand(s.getClone());
    }
    return out;
  }
}

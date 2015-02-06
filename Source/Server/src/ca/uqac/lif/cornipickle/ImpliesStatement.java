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
      m_verdict = new Verdict(Verdict.Value.FALSE);
      return m_verdict;
    }
    Statement left = m_statements.get(0);
    Statement right = m_statements.get(1);
    Verdict v_left = left.evaluate(j, d);
    /*if (v_left == Verdict.FALSE)
    {
      // Shortcut
      m_verdict = Verdict.TRUE;
      return m_verdict;
    }*/
    Verdict v_right = right.evaluate(j, d);
    // p -> q == !p or q
    Verdict n_v_left = new Verdict();
    n_v_left.negate(v_left);
    n_v_left.disjoin(v_right);
    m_verdict = n_v_left;
    return m_verdict;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
  {
    if (m_statements.size() < 2)
    {
      m_verdict = new Verdict(Verdict.Value.FALSE);
      return m_verdict;
    }
    Statement left = m_statements.get(0);
    Statement right = m_statements.get(1);
    Verdict v_left = left.evaluateAtemporal(j, d);
    /*if (v_left == Verdict.FALSE)
    {
      // Shortcut
      m_verdict = Verdict.TRUE;
      return m_verdict;
    }*/
    Verdict v_right = right.evaluateAtemporal(j, d);
    // p -> q == !p or q
    Verdict n_v_left = new Verdict();
    n_v_left.negate(v_left);
    n_v_left.disjoin(v_right);
    m_verdict = n_v_left;
    if (m_verdict.is(Verdict.Value.FALSE))
    {
      m_verdict.m_witnessFalse.add(v_left.m_witnessTrue);
      m_verdict.m_witnessFalse.add(v_right.m_witnessFalse);
    }
    else if (m_verdict.is(Verdict.Value.TRUE))
    {
      m_verdict.m_witnessTrue.add(v_left.m_witnessFalse);
      m_verdict.m_witnessTrue.add(v_right.m_witnessTrue);
    }
    return m_verdict;
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

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

import ca.uqac.lif.json.JsonElement;

public class AndStatement extends NAryStatement
{
  
  public AndStatement()
  {
    super();
  }
  
  @Override
  public String getKeyword()
  {
    return "And";
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    Verdict out = new Verdict(Verdict.Value.TRUE);
    for (Statement s : m_statements)
    {
      Verdict b = s.evaluate(j, d);
      out.conjoin(b);
      if (out.is(Verdict.Value.FALSE))
        break;
    }
    m_verdict = out;
    return m_verdict;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
  {
    Verdict out = new Verdict(Verdict.Value.TRUE);
    for (Statement s : m_statements)
    {
      Verdict b = s.evaluateAtemporal(j, d);
      out.conjoin(b);
      //if (out.is(Verdict.Value.FALSE))
        //break;
    }
    return out;
  }
  
  @Override
  public AndStatement getClone()
  {
    AndStatement out = new AndStatement();
    for (Statement s : m_statements)
    {
      out.addOperand(s.getClone());
    }
    return out;
  }

}

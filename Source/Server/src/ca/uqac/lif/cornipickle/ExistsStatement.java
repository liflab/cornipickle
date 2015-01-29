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
import ca.uqac.lif.cornipickle.json.JsonElement;

public class ExistsStatement extends ForAllStatement
{
  @Override
  public Verdict evaluate(JsonElement j, Map<String, JsonElement> d)
  {
        if (m_verdict != Statement.Verdict.INCONCLUSIVE)
    {
      return m_verdict;
    }
    // Fetch values for set
    List<JsonElement> domain = m_set.evaluate(j, d);
    // Iterate over values
    Verdict out = Verdict.FALSE;
    for (JsonElement v : domain)
    {
      Map<String,JsonElement> new_d = new HashMap<String,JsonElement>(d);
      new_d.put(m_variable.toString(), v);
      Verdict b = m_innerStatement.evaluate(j, new_d);
      out = threeValuedOr(out, b);
      if (out == Verdict.TRUE)
        break;
    }
    m_verdict = out;
    return m_verdict;
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("There exists a ").append(m_variable).append(" in ").append(m_set).append(" such that\n");
    out.append(m_innerStatement.toString(indent + "  "));
    return out.toString();
  }
  
  @Override
  public ExistsStatement getClone()
  {
    ExistsStatement out = new ExistsStatement();
    out.m_innerStatement = m_innerStatement.getClone();
    out.m_variable = m_variable;
    out.m_set = m_set;
    return out;
  }
}

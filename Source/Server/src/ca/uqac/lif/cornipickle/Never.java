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

import java.util.Iterator;
import java.util.Map;

import ca.uqac.lif.json.JsonElement;

public class Never extends Globally
{
  public Never()
  {
    super();
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    // Instantiate new inner statement
    Statement new_s = m_innerStatement.getClone();
    m_inMonitors.add(new_s);
    // Evaluate each
    Iterator<Statement> it = m_inMonitors.iterator();
    while (it.hasNext())
    {
      Statement st = it.next();
      Verdict st_v = st.evaluate(j, d);
      if (st_v.is(Verdict.Value.FALSE))
      {
        it.remove();
      }
      if (st_v.is(Verdict.Value.TRUE))
      {
        m_verdict.setValue(Verdict.Value.FALSE);
        m_verdict.setWitnessFalse(st_v.getWitnessFalse());
        break;
      }
    }
    return m_verdict;
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("Never (\n");
    out.append(m_innerStatement.toString(indent + "  "));
    out.append("\n").append(indent).append(")");
    return out.toString();
  }

  @Override
  public Statement getClone()
  {
    Never out = new Never();
    out.setInnerStatement(m_innerStatement.getClone());
    return out;
  }
}

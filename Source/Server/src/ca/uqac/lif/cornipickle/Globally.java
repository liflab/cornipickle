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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cornipickle.json.JsonElement;

public class Globally extends TemporalStatement
{
  protected Statement m_innerStatement;

  protected List<Statement> m_inMonitors;

  public Globally()
  {
    super();
    m_inMonitors = new LinkedList<Statement>();
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
  }

  @Override
  public void resetHistory()
  {
    m_inMonitors.clear();
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
  }

  public void setInnerStatement(Statement s)
  {
    m_innerStatement = s;
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
      if (st_v.is(Verdict.Value.TRUE))
      {
        it.remove();
      }
      if (st_v.is(Verdict.Value.FALSE))
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
    out.append(indent).append("Always (\n");
    out.append(m_innerStatement.toString(indent + "  "));
    out.append("\n").append(indent).append(")");
    return out.toString();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    //m_variable.prefixAccept(visitor);
    m_innerStatement.postfixAccept(visitor);
    visitor.visit(this);
    visitor.pop();
  }

  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    m_innerStatement.prefixAccept(visitor);
    visitor.pop();
  }

  @Override
  public Statement getClone()
  {
    Globally out = new Globally();
    out.setInnerStatement(m_innerStatement.getClone());
    return out;
  }
}

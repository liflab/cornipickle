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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.json.JsonElement;

public class NextTime extends TemporalStatement
{
  protected Statement m_left;

  protected Statement m_right;
  
  protected List<Statement> m_leftStatements;
  
  protected List<Statement> m_rightStatements;
  
  boolean m_firstEvent;
  
  public NextTime()
  {
    super();
    m_leftStatements = new LinkedList<Statement>();
    m_rightStatements = new LinkedList<Statement>();
    m_firstEvent = true;
  }
  
  public void setLeft(Statement s)
  {
    m_left = s;
  }
  
  public void setRight(Statement s)
  {
    m_right = s;
  }

  @Override
  public NextTime getClone()
  {
    NextTime out = new NextTime();
    out.m_left = m_left.getClone();
    out.m_right = m_right.getClone();
    return out;
  }

  @Override
  public void resetHistory()
  {
    m_left.resetHistory();
    m_right.resetHistory();
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    // Discard first event. The next time really means, the *next* time
    if (m_firstEvent == true)
    {
      m_firstEvent = false;
      return m_verdict;
    }
    m_leftStatements.add(m_left.getClone());
    m_rightStatements.add(m_right.getClone());
    int i = 0;
    while (i < m_leftStatements.size())
    {
      Statement left = m_leftStatements.get(i);
      Statement right = m_rightStatements.get(i);
      Verdict v_left = left.evaluateTemporal(j, d);
      Verdict v_right = right.evaluateTemporal(j, d);
      if (v_left.is(Verdict.Value.FALSE))
      {
        m_leftStatements.remove(i);
        m_rightStatements.remove(i);
        i--;
      }
      else if (v_left.is(Verdict.Value.TRUE) && v_right.is(Verdict.Value.FALSE))
      {
        m_verdict.setValue(Verdict.Value.FALSE);
      }
      else if (v_right.is(Verdict.Value.TRUE))
      {
        m_verdict.setValue(Verdict.Value.TRUE);
      }
      i++;
    }
    return m_verdict;
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("The next time (\n");
    out.append(m_left.toString(indent + "  "));
    out.append(")\n").append(indent).append("Then (\n");
    out.append(m_right.toString(indent + "  "));
    out.append(")");
    return out.toString();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    m_left.postfixAccept(visitor);
    m_right.postfixAccept(visitor);
    visitor.visit(this);
    visitor.pop();
  }
  
  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.pop();
    m_left.postfixAccept(visitor);
    m_right.postfixAccept(visitor);
    visitor.visit(this);
  }

}

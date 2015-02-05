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

public class Next extends TemporalStatement
{
  protected Statement m_innerStatement;

  protected boolean m_firstEvent;

  public Next()
  {
    super();
    m_firstEvent = true;
  }

  @Override
  public Next getClone()
  {
    Next out = new Next();
    out.m_innerStatement = m_innerStatement.getClone();
    return out;
  }

  @Override
  public void resetHistory()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
    m_firstEvent = true;
    m_innerStatement.resetHistory();
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    if (m_firstEvent)
    {
      m_firstEvent = false;
      return m_verdict;
    }
    if (!m_verdict.is(Verdict.Value.INCONCLUSIVE))
    {
      return m_verdict;
    }
    m_verdict = m_innerStatement.evaluate(j, d);
    return m_verdict;
  }

  public void setInnerStatement(Statement s)
  {
    m_innerStatement = s;
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("Next (\n");
    out.append(m_innerStatement.toString(indent + "  "));
    out.append("\n)");
    return out.toString();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
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

}

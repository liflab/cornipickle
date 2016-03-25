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

public class NegationStatement extends Statement
{
  protected Statement m_innerStatement;

  public NegationStatement()
  {
    super();
  }

  public void setInnerStatement(Statement s)
  {
    m_innerStatement = s;
  }

  public Statement getStatement()
  {
    return m_innerStatement;
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    m_verdict.negate(m_innerStatement.evaluate(j, d));
    return m_verdict;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
  {
    m_verdict.negate(m_innerStatement.evaluateAtemporal(j, d));
    return m_verdict;
  }

  public void resetHistory()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
    m_innerStatement.resetHistory();
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append("Not (").append(m_innerStatement).append(")");
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

  @Override
  public NegationStatement getClone()
  {
    NegationStatement out = new NegationStatement();
    out.m_innerStatement = m_innerStatement;
    return out;
  }

  @Override
  public boolean isTemporal()
  {
    return m_innerStatement.isTemporal();
  }
}

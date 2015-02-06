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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cornipickle.json.JsonElement;

public abstract class Quantifier extends Statement
{
  protected Statement m_innerStatement;

  protected StringConstant m_variable;

  protected SetExpression m_set;
  
  protected List<Statement> m_innerStatements;
  
  protected List<JsonElement> m_domain;
  
  protected Verdict.Value m_startValue;
  
  protected Verdict.Value m_cutoffValue;
  
  public Quantifier()
  {
    super();
    m_innerStatements = null;
  }
  
  protected abstract Verdict evaluationFunction(Verdict x, Verdict y, JsonElement e);
  
  public void setInnerStatement(Statement s)
  {
    m_innerStatement = s;
  }

  public String getVariable()
  {
    return m_variable.toString();
  }

  public Statement getStatement()
  {
    return m_innerStatement;
  }

  public SetExpression getSet()
  {
    return m_set;
  }

  public void setVariable(StringConstant s)
  {
    m_variable = s;
  }

  public void setVariable(String s)
  {
    m_variable = new StringConstant(s);
  }

  public void setDomain(SetExpression s)
  {
    m_set = s;
  }
  
  public void resetHistory()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
    m_domain = null;
    m_innerStatement.resetHistory();
  }
  
  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    //m_variable.prefixAccept(visitor);
    m_innerStatement.postfixAccept(visitor);
    m_set.prefixAccept(visitor);
    visitor.visit(this);
    visitor.pop();
  }

  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    m_innerStatement.prefixAccept(visitor);
    m_set.prefixAccept(visitor);
    visitor.pop();
  }
  
  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    if (m_domain == null)
    {
      // Fetch values for set; create one instance of the
      // inner statement for each value
      m_domain = m_set.evaluate(j, d);
      int domain_size = m_domain.size();
      m_innerStatements = new LinkedList<Statement>();
      for (int i = 0; i < domain_size; i++)
      {
        m_innerStatements.add(m_innerStatement.getClone());
      }
    }
    // Iterate over values
    Verdict out = new Verdict(m_startValue);
    int i = 0;
    for (JsonElement v : m_domain)
    {
      Statement in_s = m_innerStatements.get(i);
      Map<String,JsonElement> new_d = new HashMap<String,JsonElement>(d);
      new_d.put(m_variable.toString(), v);
      Verdict b = in_s.evaluate(j, new_d);
      out = evaluationFunction(out, b, v);
      i++;
      if (out.is(m_cutoffValue))
        break;
    }
    m_verdict = out;
    return m_verdict;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
  {
    List<JsonElement> domain = m_set.evaluate(j, d);
    // Iterate over values
    Verdict out = new Verdict(m_startValue);
    for (JsonElement v : domain)
    {
      Map<String,JsonElement> new_d = new HashMap<String,JsonElement>(d);
      new_d.put(m_variable.toString(), v);
      Verdict b = m_innerStatement.evaluateAtemporal(j, new_d);
      out = evaluationFunction(out, b, v);
      if (out.is(m_cutoffValue))
        break;
    }
    return out;
  }
  
  @Override
  public boolean isTemporal()
  {
    return m_innerStatement.isTemporal();
  }
}

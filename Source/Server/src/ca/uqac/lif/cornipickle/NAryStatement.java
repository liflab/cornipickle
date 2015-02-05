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

public abstract class NAryStatement extends Statement
{
  protected LinkedList<Statement> m_statements;
  
  public NAryStatement()
  {
    m_statements = new LinkedList<Statement>();
  }
  
  public final void addOperand(Statement s)
  {
    m_statements.add(s);
  }
  
  public abstract String getKeyword();
  
  public List<Statement> getStatements()
  {
    return m_statements;
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    for (Statement n : m_statements)
    {
      n.postfixAccept(visitor);
    }
    visitor.visit(this);
    visitor.pop();
  }
  
  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    for (Statement n : m_statements)
    {
      n.prefixAccept(visitor);
    }
    visitor.pop();
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    boolean first = true;
    for (Statement s : m_statements)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        out.append(" ").append(getKeyword()).append(" ");
      }
      out.append("(").append(s).append(")");
    }
    return out.toString();
  }
  
  @Override
  public void resetHistory()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
    for (Statement n : m_statements)
    {
      n.resetHistory();
    }
  }
  
  @Override
  public boolean isTemporal()
  {
    for (Statement s : m_statements)
    {
      if (s.isTemporal())
      {
        return true;
      }
    }
    return false;
  }

}

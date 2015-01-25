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

public class ForAllStatement extends Statement
{
  public ForAllStatement()
  {
    super();
  }
  
  protected Statement m_innerStatement;
  
  protected StringConstant m_variable;
  
  protected SetExpression m_set;
  
  public void setInnerStatement(Statement s)
  {
    m_innerStatement = s;
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

  @Override
  public boolean evaluate(JsonElement j, Map<String, JsonElement> d)
  {
    // Fetch values for set
    List<JsonElement> domain = m_set.evaluate(j, d);
    // Iterate over values
    boolean out = true;
    for (JsonElement v : domain)
    {
      Map<String,JsonElement> new_d = new HashMap<String,JsonElement>(d);
      new_d.put(m_variable.toString(), v);
      boolean b = m_innerStatement.evaluate(j, new_d);
      out = out && b;
      if (out == false)
        break;
    }
    return out;
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("For each ").append(m_variable).append(" in ").append(m_set).append("\n");
    out.append(m_innerStatement.toString(indent + "  "));
    return out.toString();
  }
}

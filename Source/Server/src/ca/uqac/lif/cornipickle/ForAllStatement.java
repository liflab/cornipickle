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

import ca.uqac.lif.cornipickle.json.JsonElement;

public class ForAllStatement extends Quantifier
{
  public ForAllStatement()
  {
    super();
    m_startValue = Verdict.Value.TRUE;
    m_cutoffValue = Verdict.Value.FALSE;
  }

  protected Verdict evaluationFunction(Verdict x, Verdict y, JsonElement e)
  {
    x.conjoin(y, e);
    return x;
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("For each ").append(m_variable).append(" in ").append(m_set).append("\n");
    out.append(m_innerStatement.toString(indent + "  "));
    return out.toString();
  }

  @Override
  public ForAllStatement getClone()
  {
    ForAllStatement out = new ForAllStatement();
    out.m_innerStatement = m_innerStatement.getClone();
    out.m_variable = m_variable;
    out.m_set = m_set.getClone();
    return out;
  }
}

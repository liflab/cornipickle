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

public class OrStatement extends AndStatement
{
  public OrStatement()
  {
    super();
  }
  
  @Override
  public String getKeyword()
  {
    return "Or";
  }
  
  @Override
  public boolean evaluate(JsonElement j, Map<String, JsonElement> d)
  {
    if (m_statements.isEmpty())
    {
      return false;
    }
    boolean out = false;
    for (Statement s : m_statements)
    {
      boolean b = s.evaluate(j, d);
      out = out || b;
      if (out)
        break;
    }
    return out;
  }
}

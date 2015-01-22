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
import ca.uqac.lif.cornipickle.json.JsonNumber;
import ca.uqac.lif.cornipickle.json.JsonString;

public abstract class ComparisonStatement extends Statement
{
  protected Property m_left;
  protected Property m_right;
  
  public final boolean evaluate(JsonElement j, Map<String,JsonElement> d)
  {
    JsonElement e1 = m_left.evaluate(j, d);
    JsonElement e2 = m_right.evaluate(j, d);
    return compare(e1, e2);
  }
  
  public void setLeft(final Property p)
  {
    m_left = p;
  }
  
  public void setRight(final Property p)
  {
    m_right = p;
  }
  
  protected boolean compare(JsonElement e1, JsonElement e2)
  {
    if (e1 instanceof JsonString && e2 instanceof JsonString)
    {
      return compare((JsonString) e1, (JsonString) e2);
    }
    else if (e1 instanceof JsonNumber && e2 instanceof JsonNumber)
    {
      return compare((JsonNumber) e1, (JsonNumber) e2);
    }
    return false;
  }
  
  protected abstract boolean compare(JsonString e1, JsonString e2);
  
  protected abstract boolean compare(JsonNumber e1, JsonNumber e2);
}

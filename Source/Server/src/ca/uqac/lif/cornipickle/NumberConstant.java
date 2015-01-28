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

public class NumberConstant extends Constant
{
  protected final JsonNumber m_value;
  
  public NumberConstant(Number n)
  {
    super();
    m_value = new JsonNumber(n);
  }
  
  public NumberConstant(String s)
  {
    super();
    m_value = new JsonNumber(Float.parseFloat(s));
  }
  
  public NumberConstant(JsonNumber n)
  {
    super();
    m_value = n;
  }

  @Override
  public JsonElement evaluate(JsonElement t, Map<String, JsonElement> d)
  {
    return m_value;
  }
  
  @Override
  public String toString(String indent)
  {
    return m_value.toString();
  }

}

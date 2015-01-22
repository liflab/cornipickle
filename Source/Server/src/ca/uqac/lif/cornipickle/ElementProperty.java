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
import ca.uqac.lif.cornipickle.json.JsonPath;

public class ElementProperty extends Property
{
  protected String m_elementName;
  
  protected String m_propertyName;
  
  public ElementProperty(StringConstant elementName, StringConstant propertyName)
  {
    super();
    setElementName(elementName.toString());
    setPropertyName(propertyName.toString());
  }
  
  public ElementProperty(String elementName, String propertyName)
  {
    super();
    setElementName(elementName);
    setPropertyName(propertyName);
  }
  
  public void setElementName(String n)
  {
    m_elementName = n;
  }
  
  public void setPropertyName(String n)
  {
    m_propertyName = n;
  }

  @Override
  public JsonElement evaluate(JsonElement t, Map<String, JsonElement> d)
  {
    // Fetch element
    JsonElement e = d.get(m_elementName);
    // Get its value
    JsonElement v = JsonPath.get(e, m_propertyName);
    // Return
    return v;
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(m_elementName).append("'s ").append(m_propertyName);
    return out.toString();
  }

}

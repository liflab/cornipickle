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
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.json.JsonMap;
import ca.uqac.lif.cornipickle.json.JsonPath;
import ca.uqac.lif.cornipickle.json.JsonString;

public abstract class ElementProperty extends Property
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
  
  public String getElementName()
  {
    return m_elementName;
  }
  
  public String getPropertyName()
  {
    return m_propertyName;
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
    if (m_propertyName.compareToIgnoreCase("value") == 0)
    {
      return e;
    }
    else if (m_propertyName.compareToIgnoreCase("text") == 0)
    {
      if (!(e instanceof JsonMap))
      {
        assert false; // Should not happen
        return null;
      }
      // Text is a recursive property: concatenate the text of all children
      JsonMap jm = (JsonMap) e;
      String s = getClearText(jm);
      JsonString js = new JsonString(s);
      return js;
    }
    JsonElement v = JsonPath.get(e, m_propertyName);
    // Return
    return v;
  }
  
  protected static String getClearText(JsonMap t)
  {
    StringBuilder sb = getClearTextRecursive(t);
    String out = sb.toString();
    // Replace all multiple whitespace by a single one
    out = out.replaceAll("\\s+", " ");
    return out.trim();
  }
  
  protected static StringBuilder getClearTextRecursive(JsonMap t)
  {
    StringBuilder out = new StringBuilder();
    JsonElement jt = t.get("text");
    if (jt != null && jt instanceof JsonString)
    {
      JsonString js = (JsonString) jt;
      out.append(js.stringValue()).append(" ");
    }
    JsonList children_list = (JsonList) t.get("children");
    if (children_list != null)
    {
      for (JsonElement e : children_list)
      {
        if (e instanceof JsonMap)
        {
          out.append(getClearText((JsonMap) e)).append(" ");        
        }
      }
    }
    return out;
  }

}

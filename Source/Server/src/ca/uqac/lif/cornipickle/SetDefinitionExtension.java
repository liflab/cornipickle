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
import java.util.Map;

import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonString;

public class SetDefinitionExtension extends SetDefinition
{
  protected List<JsonElement> m_elements;
  
  public SetDefinitionExtension(StringConstant name)
  {
    super(name);
    m_elements = new LinkedList<JsonElement>();
  }
  
  public SetDefinitionExtension(String name)
  {
    this(new StringConstant(name));
  }
  
  public List<JsonElement> getElements()
  {
    return m_elements;
  }
  
  public SetDefinitionExtension(StringConstant name, ElementList elements)
  {
    this(name);
    for (LanguageElement el : elements)
    {
      JsonElement c_el = toJsonElement(el);
      if (c_el != null)
      {
        m_elements.add(c_el);
      }
    }
  }
  
  public void addElement(LanguageElement e)
  {
    JsonElement c_e = toJsonElement(e);
    if (c_e != null)
    {
      m_elements.add(c_e);
    }
  }
  
  protected static JsonElement toJsonElement(LanguageElement e)
  {
    if (e instanceof StringConstant)
    {
      StringConstant s = (StringConstant) e;
      return new JsonString(s.toString());
    }
    else if (e instanceof NumberConstant)
    {
      NumberConstant s = (NumberConstant) e;
      return s.m_value;      
    }
    return null;
  }

  @Override
  public List<JsonElement> evaluate(JsonElement t, Map<String, JsonElement> d)
  {
    return m_elements;
  }
  
  public String getSetName()
  {
    return m_setName.toString();
  }

  @Override
  public String toString(String indent)
  {
    return m_setName.toString();
  }

}

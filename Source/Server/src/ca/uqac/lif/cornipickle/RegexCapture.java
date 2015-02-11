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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonString;

public class RegexCapture extends SetExpression
{
  protected ElementPropertyPossessive m_variable;
  
  protected Pattern m_pattern;
  
  public void setPattern(Pattern pat)
  {
    m_pattern = pat;
  }
  
  public void setPattern(String s)
  {
    m_pattern = Pattern.compile(s);
  }
  
  public String getPattern()
  {
    return m_pattern.toString();
  }
  
  public void setProperty(ElementPropertyPossessive v)
  {
    m_variable = v;
  }
  
  public String getVariable()
  {
    return m_variable.toString();
  }

  @Override
  public List<JsonElement> evaluate(JsonElement t, Map<String, JsonElement> d)
  {
    List<JsonElement> out = new LinkedList<JsonElement>();
    JsonElement value = m_variable.evaluate(t, d);
    if (!(value instanceof JsonString))
    {
      return out;
    }
    JsonString text = (JsonString) value;
    Matcher mat = m_pattern.matcher(text.stringValue());
    while (mat.find())
    {
      if (mat.groupCount() >= 1)
      {
        String val = mat.group(1);
        out.add(new JsonString(val));
      }
    }
    return out;
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append("match ").append(m_variable).append(" with ").append(m_pattern);
    return out.toString();
  }

  @Override
  public RegexCapture getClone()
  {
    RegexCapture out = new RegexCapture();
    out.m_variable = m_variable.getClone();
    out.m_pattern = m_pattern;
    return out;
  }

}

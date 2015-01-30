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
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.json.JsonMap;
import ca.uqac.lif.cornipickle.json.JsonPath;

public class CssSelector extends SetExpression
{
  String m_cssSelector;
  
  public CssSelector(String selector)
  {
    super();
    m_cssSelector = selector;
  }
  
  public CssSelector(StringConstant selector)
  {
    super();
    m_cssSelector = selector.toString();
  }
  
  public void mergeWith(CssSelector right)
  {
    if (right == null)
      return;
    m_cssSelector =  right.m_cssSelector + " " + m_cssSelector;
  }
  
  public String getSelector()
  {
    return m_cssSelector;
  }
  
  @Override
  public List<JsonElement> evaluate(JsonElement j, Map<String, JsonElement> d)
  {
    if (!(j instanceof JsonMap))
    {
      assert false;
      return null; // Should not happen
    }
    if (m_result != null)
    {
      return m_result;
    }
    m_result = fetch(m_cssSelector, (JsonMap) j);
    return m_result;
  }
  
  protected static List<JsonElement> fetch(String css_expression, JsonMap root)
  {
    String[] css_parts = css_expression.split(" ");
    List<String> css_list = new LinkedList<String>();
    for (String part : css_parts)
    {
      css_list.add(part);
    }
    return fetch(css_list, root);
  }

  protected static List<JsonElement> fetch(List<String> css_expression, JsonMap root)
  {
    List<JsonElement> out = new LinkedList<JsonElement>();

    if (css_expression.isEmpty())
    {
      return out;
    }
    assert !css_expression.isEmpty();

    // Some of these may be null
    String el_tag_name = JsonPath.getString(root, "tagname");
    String el_class_name = JsonPath.getString(root, "class");
    String el_id_name = JsonPath.getString(root, "id");

    LinkedList<String> new_css_expression = null;
    String first_part = css_expression.get(0);
    // Split CSS part into tag, class and id
    Pattern pat = Pattern.compile("([\\w\\d]+){0,1}(\\.([\\w\\d]+)){0,1}(#([\\w\\d]+)){0,1}");
    Matcher mat = pat.matcher(first_part);
    if (!mat.find())
    {
      assert false; // Invalid CSS selector
    }
    String sel_tag_name = mat.group(1);
    String sel_class_name = mat.group(3);
    String sel_id_name = mat.group(5);
    if ((sel_tag_name == null || (el_tag_name != null && el_tag_name.compareTo(sel_tag_name) == 0))
        && (sel_class_name == null || (el_class_name != null && containsClass(el_class_name, sel_class_name)))
        && (sel_id_name == null || (el_id_name != null && el_id_name.compareTo(sel_id_name) == 0)))
    {
      if (css_expression.size() == 1)
      {
        out.add(root);
      }
      new_css_expression = new LinkedList<String>(css_expression);
      new_css_expression.removeFirst();
    }
    JsonList children = JsonPath.getList(root, "children");
    if (children != null)
    {
      for (JsonElement child : children)
      {
        if (child instanceof JsonMap)
        {
          JsonMap m_child = (JsonMap) child;
          out.addAll(fetch(css_expression, m_child));
          if (new_css_expression != null)
          {
            out.addAll(fetch(new_css_expression, m_child));
          }
        }
        else
        {
          assert false; // Should not happen
        }
      }
    }
    return out;
  }
  
  protected static boolean containsClass(String element_classes, String target_class)
  {
  	String[] parts = element_classes.split(" ");
  	for (String part : parts)
  	{
  		if (part.compareTo(target_class) == 0)
  		{
  			return true;
  		}
  	}
  	return false;
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append("$(").append(m_cssSelector).append(")");
    return out.toString();
  }
  
  @Override
  public CssSelector getClone()
  {
    CssSelector out = new CssSelector(m_cssSelector);
    return out;
  }

}

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

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonPath;

public class CssSelector extends SetExpression
{
  String m_cssSelector;
  
  CssSelector()
  {
	  super();
	  m_cssSelector = "";
  }
  
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
    return fetch(m_cssSelector, (JsonMap) j);
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
    String el_tag_name = JsonPath.getString(root, "tagname");
    String el_class_name = JsonPath.getString(root, "class");
    String el_id_name = JsonPath.getString(root, "id");
    LinkedList<String> new_css_expression = null;
    String first_part = css_expression.get(0);
    CssPathElement cpe = new CssPathElement(first_part);
    if (cpe.matches(el_tag_name, el_class_name, el_id_name))
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
  
  protected static class CssPathElement
  {
    protected String m_tagName;
    protected String m_className;
    protected String m_idName;
    
    public CssPathElement(String s)
    {
      super();  
      parseFromString(s);
    }
    
    protected void parseFromString(String first_part)
    {
      // Split CSS part into tag, class and id
      Pattern pat = Pattern.compile("([\\w\\d]+){0,1}(\\.([\\w\\d]+)){0,1}(#([\\w\\d]+)){0,1}");
      Matcher mat = pat.matcher(first_part);
      if (!mat.find())
      {
        assert false; // Invalid CSS selector
      }
      m_tagName = mat.group(1);
      m_className = mat.group(3);
      m_idName = mat.group(5);          
    }
    
    /**
     * Checks whether an element's tag, class and ID name match the
     * CSS selector element.
     * @param tagName The tag name
     * @param className The class name
     * @param idName The ID name
     * @return True if element matches the selector, false otherwise
     */
    public boolean matches(String tagName, String className, String idName)
    {
      if (m_tagName != null)
      {
        if (tagName == null)
        {
          return false;
        }
        if (m_tagName.compareTo(tagName) != 0)
        {
          return false;
        }
      }
      if (m_className != null)
      {
        if (className == null)
        {
          return false;
        }
        if (!containsClass(className, m_className))
        {
          return false;
        }
      }
      if (m_idName != null)
      {
        if (idName == null)
        {
          return false;
        }
        if (m_idName.compareTo(idName) != 0)
        {
          return false;
        }
      }
      return true;
    }
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

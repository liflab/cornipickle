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

import ca.uqac.lif.cornipickle.server.Main;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonPath;
import ca.uqac.lif.json.JsonString;

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
		m_cssSelector = right.m_cssSelector + " " + m_cssSelector;
	}
	
	public void mergeAsChildWith(CssSelector right)
	{
	  if (right == null)
	    return;
	  m_cssSelector = right.m_cssSelector + ">" + m_cssSelector;
	}
	
	public void mergeAsAndWith(CssSelector right)
  {
    if (right == null)
      return;
    m_cssSelector = right.m_cssSelector + "," + m_cssSelector;
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
		if(m_cssSelector.equals("*"))
    {
	    return fetchAllChildrenRecursively(((JsonList)((JsonMap)j).get("children")).get(0));
    }
		return fetch(m_cssSelector, (JsonMap) j, d);
	}

	protected static List<JsonElement> fetch(String css_expression, JsonMap root, Map<String, JsonElement> d)
	{
	  List<JsonElement> out = new LinkedList<JsonElement>();
	  String[] css_or = css_expression.split(",");
	  for(String part_or : css_or)
	  {
	    String[] css_parts = part_or.split(" ");
      List<String> css_list = new LinkedList<String>();
      for (String part : css_parts)
      {
        css_list.add(part);
      }
      out.addAll(fetch(css_list, root, d));
	  }
	  return out;
	}

	protected static List<JsonElement> fetch(List<String> css_expression, JsonMap root, Map<String, JsonElement> d)
	{
		List<JsonElement> out = new LinkedList<JsonElement>();

		if (css_expression.isEmpty())
		{
			return out;
		}
		assert !css_expression.isEmpty();
		String el_tag_name = "";
		String el_class_name = "";
		String el_id_name = "";
		if (Main.s_platform == Main.PlatformType.ANDROID_NATIVE)
		{
			
			el_tag_name = JsonPath.getString(root, "element");// tagname //																// element
			el_class_name = JsonPath.getString(root, "tag");// tag // tag
			el_id_name = JsonPath.getString(root, "id");
			
		} else
		{

			el_tag_name = JsonPath.getString(root, "tagname");// tagname //																// element
			el_class_name = JsonPath.getString(root, "class");// tag // tag
			el_id_name = JsonPath.getString(root, "id");

		}
		LinkedList<String> new_css_expression = null;
		String first_part = css_expression.get(0);
		
		int index_of_arrow = first_part.indexOf('>');
		String rest_first_part = null;
		boolean isRightPartOfArrow = false;
		if(index_of_arrow != -1)
		{
		  if(index_of_arrow == 0)
		  {
		    first_part = first_part.substring(1);
		    isRightPartOfArrow = true;
		  }
		  
		  index_of_arrow = first_part.indexOf('>');
		  if(index_of_arrow != -1) //There's an arrow so we split in 2 and push them as 2 separate parts in the front
		  {
		    rest_first_part = first_part.substring(index_of_arrow);
        first_part = first_part.substring(0, index_of_arrow);
        css_expression.remove(0);
        css_expression.add(0, rest_first_part);
        css_expression.add(0, first_part);
		  }
		}
		
		boolean matches = false;
		
		if(first_part.charAt(0) == '$')
		{
		  JsonMap element = (JsonMap)d.get(first_part);
		  if(element == null)
		  {
		    return out;
		  }
		  
		  JsonElement corniid = root.get("cornipickleid");
		  if(element.get("cornipickleid").compareTo(corniid) == 0)
		  {
		    matches = true;
		  }
		}
		else if(first_part.charAt(0) == '*')
		{
		  if(!isRightPartOfArrow)
		  {
		    return fetchAllChildrenRecursively(root);
		  }
		  if(el_tag_name.equals("CDATA"))
		  {
		    matches = false;
		  }
		  else
		  {
		    matches = true;
		  }
		}
		else
		{
		  CssPathElement cpe = new CssPathElement(first_part);
		  if(cpe.matches(el_tag_name, el_class_name, el_id_name))
		  {
		    matches = true;
		  }
		}
		
		if (matches)
		{
			if (css_expression.size() == 1)
			{
				out.add(root);
			}
			new_css_expression = new LinkedList<String>(css_expression);
			new_css_expression.removeFirst();
		}
		else if(isRightPartOfArrow) //It had to be direct child but it wasn't
		{
		  return out;
		}
		
		JsonList children = JsonPath.getList(root, "children");
		if (children != null)
		{
			for (JsonElement child : children)
			{
				if (child instanceof JsonMap)
				{
					JsonMap m_child = (JsonMap) child;
					if(!isRightPartOfArrow)
					{
					  out.addAll(fetch(css_expression, m_child, d));
					}
					if (new_css_expression != null)
					{
						out.addAll(fetch(new_css_expression, m_child, d));
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
	
	protected static List<JsonElement> fetchAllChildrenRecursively(JsonElement j)
	{
	  if(!(j instanceof JsonMap))
	  {
	    assert false;
	  }
	  JsonMap element = (JsonMap)j;
	  List<JsonElement> out = new LinkedList<JsonElement>();
	  if(((JsonString)element.get("tagname")).stringValue().equals("CDATA"))
	  {
	    return out;
	  }
	  else
	  {
	    out.add(element);
	  }
	  if(element.containsKey("children"))
	  {
	    for(JsonElement child : (JsonList)element.get("children"))
	    {
	      if(!(child instanceof JsonMap))
	      {
	        assert false;
	      }
	      out.addAll(fetchAllChildrenRecursively((JsonMap)child));
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
			Pattern pat = Pattern.compile("^\\*|(?:([\\w]+){0,1}(\\.([\\w-]+)){0,1}(#([\\w-]+)){0,1})$");
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
		 * Checks whether an element's tag, class and ID name match the CSS
		 * selector element.
		 * 
		 * @param tagName
		 *            The tag name
		 * @param className
		 *            The class name
		 * @param idName
		 *            The ID name
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
		for (String part : parts) {
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

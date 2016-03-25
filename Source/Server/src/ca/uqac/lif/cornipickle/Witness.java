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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ca.uqac.lif.json.JsonElement;

/**
 * Pointer to an element "explaining" why a statement is not fulfilled
 * @author sylvain
 */
public class Witness
{
  protected List<Witness> m_children;
  
  protected JsonElement m_element;
  
  public Witness()
  {
    super();
    m_children = new LinkedList<Witness>();
  }
  
  public Witness(JsonElement e)
  {
    this();
    m_element = e;
  }
  
  public void add(Witness w)
  {
    if (!w.isEmpty())
    {
      // Avoid filling witness with nested empty lists
      m_children.add(w);
    }
  }
  
  public void setElement(JsonElement e)
  {
    m_element = e;
  }
  
  public int childrenCount()
  {
    return m_children.size();
  }
  
  public boolean isEmpty()
  {
    return m_children.isEmpty() && m_element == null;
  }
  
  @Override
  public final String toString()
  {
    return toString("");
  }
  
  protected String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent);
    if (m_element != null)
    {
      out.append(m_element).append(":");
    }
    out.append("{\n");
    boolean first = true;
    for (Witness child : m_children)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        out.append(",\n");
      }
      out.append(child.toString(indent + " "));
    }
    out.append("\n").append(indent).append("}");
    return out.toString();
  }
  
  public Set<Set<JsonElement>> flatten()
  {
    return flatten(new HashSet<JsonElement>());
  }
  
  protected Set<Set<JsonElement>> flatten(Set<JsonElement> current)
  {
    HashSet<Set<JsonElement>> out = new HashSet<Set<JsonElement>>();
    if (m_element != null)
    {
      current.add(m_element);
    }
    if (m_children.isEmpty())
    {
      out.add(current);
      return out;
    }
    for (Witness child : m_children)
    {
      Set<JsonElement> new_current = new HashSet<JsonElement>();
      new_current.addAll(current);
      out.addAll(child.flatten(new_current));
    }
    return out;
  }
}

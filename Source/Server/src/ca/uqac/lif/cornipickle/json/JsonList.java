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
package ca.uqac.lif.cornipickle.json;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class JsonList extends JsonElement implements List<JsonElement>
{
  protected List<JsonElement> m_list;
  
  public JsonList()
  {
    super();
    m_list = new LinkedList<JsonElement>();
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("[\n");
    boolean first = true;
    for (JsonElement e : this)
    {
      if (first)
        first = false;
      else
        out.append(",\n");
      out.append(e.toString(indent + "  "));
    }
    out.append("\n").append(indent).append("]");
    return out.toString();
  }

  @Override
  public boolean add(JsonElement e)
  {
    return m_list.add(e);
  }

  @Override
  public void add(int index, JsonElement element)
  {
    m_list.add(index, element);
  }

  @Override
  public boolean addAll(Collection<? extends JsonElement> c)
  {
    return m_list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends JsonElement> c)
  {
    return m_list.addAll(index, c);
  }

  @Override
  public void clear()
  {
    m_list.clear();
  }

  @Override
  public boolean contains(Object o)
  {
    return m_list.contains(o);
  }

  @Override
  public boolean containsAll(Collection<?> c)
  {
    return m_list.containsAll(c);
  }

  @Override
  public JsonElement get(int index)
  {
    return m_list.get(index);
  }

  @Override
  public int indexOf(Object o)
  {
    return m_list.indexOf(o);
  }

  @Override
  public boolean isEmpty()
  {
    return m_list.isEmpty();
  }

  @Override
  public Iterator<JsonElement> iterator()
  {
    return m_list.iterator();
  }

  @Override
  public int lastIndexOf(Object o)
  {
    return m_list.lastIndexOf(o);
  }

  @Override
  public ListIterator<JsonElement> listIterator()
  {
    return m_list.listIterator();
  }

  @Override
  public ListIterator<JsonElement> listIterator(int index)
  {
    return m_list.listIterator(index);
  }

  @Override
  public boolean remove(Object o)
  {
    return m_list.remove(o);
  }

  @Override
  public JsonElement remove(int index)
  {
    return m_list.remove(index);
  }

  @Override
  public boolean removeAll(Collection<?> c)
  {
    return m_list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c)
  {
    return m_list.retainAll(c);
  }

  @Override
  public JsonElement set(int index, JsonElement element)
  {
    return m_list.set(index, element);
  }

  @Override
  public int size()
  {
    return m_list.size();
  }

  @Override
  public List<JsonElement> subList(int fromIndex, int toIndex)
  {
    return m_list.subList(fromIndex, toIndex);
  }

  @Override
  public Object[] toArray()
  {
    return m_list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a)
  {
    return m_list.toArray(a);
  }

}

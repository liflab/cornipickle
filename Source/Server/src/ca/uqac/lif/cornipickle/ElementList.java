package ca.uqac.lif.cornipickle;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ElementList extends LanguageElement implements List<LanguageElement>
{
  protected List<LanguageElement> m_list;
  
  public ElementList()
  {
    super();
    m_list = new LinkedList<LanguageElement>();
  }

  @Override
  public String toString(String indent)
  {
    return m_list.toString();
  }
  
  @Override
  public boolean add(LanguageElement e)
  {
    return m_list.add(e);
  }

  @Override
  public void add(int index, LanguageElement element)
  {
    m_list.add(index, element);
  }

  @Override
  public boolean addAll(Collection<? extends LanguageElement> c)
  {
    return m_list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends LanguageElement> c)
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
  public LanguageElement get(int index)
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
  public Iterator<LanguageElement> iterator()
  {
    return m_list.iterator();
  }

  @Override
  public int lastIndexOf(Object o)
  {
    return m_list.lastIndexOf(o);
  }

  @Override
  public ListIterator<LanguageElement> listIterator()
  {
    return m_list.listIterator();
  }

  @Override
  public ListIterator<LanguageElement> listIterator(int index)
  {
    return m_list.listIterator(index);
  }

  @Override
  public boolean remove(Object o)
  {
    return m_list.remove(o);
  }

  @Override
  public LanguageElement remove(int index)
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
  public LanguageElement set(int index, LanguageElement element)
  {
    return m_list.set(index, element);
  }

  @Override
  public int size()
  {
    return m_list.size();
  }

  @Override
  public List<LanguageElement> subList(int fromIndex, int toIndex)
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

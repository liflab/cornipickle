/*
    Automated fault localization in discrete structures
    Copyright (C) 2016-2017 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cornipickle.math;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Powerset iterator that operates in a "negative" way. Every time it picks
 * a new element in its solution, it asks an element filter to remove from the
 * set of available elements those that would conflict with the newly added
 * element.
 * @author Sylvain Hallé
 *
 * @param <T> The type of the elements from the original set
 */
public class NegativePowersetIterator<T> extends PowersetIterator<T>
{
	protected List<T> m_orderedElements;
	
	protected int m_currentSize;
	
	protected ArrayDeque<StackFrame> m_stack;
	
	protected Set<T> m_nextSolution;
	
	protected int m_maxSize = -1;
	
	protected boolean m_done = false;
	
	protected ElementFilter<T> m_elementFilter;
	
	public NegativePowersetIterator(Set<? extends T> elements, ElementFilter<T> element_filter)
	{
		super(elements);
		m_orderedElements = new ArrayList<T>(elements.size());
		m_orderedElements.addAll(elements);
		m_stack = new ArrayDeque<StackFrame>();
		m_elementFilter = element_filter;
		reset();
	}
	
	public NegativePowersetIterator(Set<T> elements)
	{
		super(elements);
		m_orderedElements = new ArrayList<T>(elements.size());
		m_orderedElements.addAll(elements);
		m_stack = new ArrayDeque<StackFrame>();
		m_elementFilter = null;
		reset();
	}
	
	@Override
	public boolean hasNext() 
	{
		if (m_done || m_timeoutExceeded)
		{
			return false;
		}
		while (m_currentSize < m_elements.size())
		{
			if (m_stack.isEmpty())
			{
				m_currentSize++;
				if (m_currentSize == 0)
				{
					m_nextSolution = new HashSet<T>();
					return true;
				}
				if (m_maxSize > 0 && m_currentSize > m_maxSize)
				{
					// Done
					m_done = true;
					return false;
				}
				m_stack.push(new StackFrame(m_orderedElements));
			}
			while (!m_stack.isEmpty())
			{
				StackFrame frame = m_stack.peek();
				StackFrame next = frame.nextFrame();
				if (next == null)
				{
					m_stack.pop();
					continue;
				}
				m_stack.push(next);
				if (next.m_elements.size() == m_currentSize)
				{
					m_nextSolution = next.m_elements;
					return true;
				}
			}			
		}
		m_done = true;
		return false;
	}

	@Override
	public Set<T> next()
	{
		return m_nextSolution;
	}

	@Override
	public void reset()
	{
		super.reset();
		m_currentSize = -1;
		m_stack.clear();
		m_nextSolution = null;
		m_done = false;
	}
	
	/**
	 * Representation of a specific state of the enumeration of elements
	 * in the set
	 */
	protected class StackFrame
	{
		public Set<T> m_elements;
		public List<T> m_availableElements;
		public int m_position;
		
		public StackFrame(List<T> available_elements)
		{
			super();
			m_elements = new HashSet<T>();
			m_availableElements = new ArrayList<T>(available_elements.size());
			m_availableElements.addAll(available_elements);
			m_position = -1;
		}
		
		protected StackFrame(List<T> available_elements, Set<T> current_elements)
		{
			super();
			m_elements = current_elements;
			m_availableElements = available_elements;
			m_position = -1;
		}
		
		public StackFrame nextFrame()
		{
			m_position++;
			if (m_position >= m_availableElements.size())
			{
				return null;
			}
			if (m_elements.size() == m_currentSize)
			{
				return null;
			}
			Set<T> elems = new HashSet<T>();
			elems.addAll(m_elements);
			T e = m_availableElements.get(m_position);
			elems.add(e);
			List<T> avail_elems = new ArrayList<T>(m_availableElements.size());
			avail_elems.addAll(m_availableElements);
			for (int i = 0; i <= m_position; i++)
			{
				avail_elems.remove(0);
			}
			if (m_elementFilter != null)
			{
				m_elementFilter.filter(avail_elems, e);
			}
			StackFrame frame = new StackFrame(avail_elems, elems);
			return frame;
		}
		
		@Override
		public String toString()
		{
			return "{" + m_elements + ", " + m_availableElements + ", " + m_position + "}";
		}
	}
	
	public static interface ElementFilter<T>
	{
		public abstract void filter(Collection<? extends T> elements, T element);
	}
	
}

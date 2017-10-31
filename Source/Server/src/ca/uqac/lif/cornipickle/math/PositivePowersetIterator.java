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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PositivePowersetIterator<T> extends PowersetIterator<T>
{
	private Iterator<Set<T>> m_iterator;
	
	private Set<Set<T>> m_currentSolutions;
	
	/**
	 * An optional filter to discard some of the generated subsets
	 */
	protected SolutionFilter<T> m_solutionFilter;
	
	public PositivePowersetIterator(Set<? extends T> transformations, SolutionFilter<T> filter)
	{
		super(transformations);
		m_solutionFilter = filter;
		m_currentSolutions = new HashSet<Set<T>>();
		reset();
	}
	
	public PositivePowersetIterator(Set<T> elements)
	{
		super(elements);
		m_solutionFilter = new PassthroughFilter<T>();
		m_currentSolutions = new HashSet<Set<T>>();
		reset();
	}
	
	public void reset()
	{
		super.reset();
		m_currentSolutions.clear();
		m_currentSolutions.add(new HashSet<T>());
		m_iterator = m_currentSolutions.iterator();
	}

	@Override
	public boolean hasNext()
	{
		if (m_timeoutExceeded)
		{
			return false;
		}
		m_startTime = System.currentTimeMillis();
		if (m_iterator.hasNext())
		{
			return true;
		}
		Set<Set<T>> new_solutions = new HashSet<Set<T>>();
		for (Set<T> sol : m_currentSolutions)
		{
			if (isTooLong())
			{
				return false;
			}
			for (T e : m_elements)
			{
				if (!m_solutionFilter.canAdd(sol, e))
				{
					continue;
				}
				Set<T> new_sol = new HashSet<T>();
				new_sol.addAll(sol);
				if (new_sol.contains(e))
				{
					continue;
				}
				new_sol.add(e);
				if (new_solutions.contains(new_sol))
				{
					continue;
				}
				new_solutions.add(new_sol);
			}
		}
		if (new_solutions.isEmpty())
		{
			return false;
		}
		m_currentSolutions = new_solutions;
		m_iterator = m_currentSolutions.iterator();
		return true;
	}
		
	protected static boolean sameSet(Set<?> s1, Set<?> s2)
	{
		return s1.containsAll(s2) && s2.containsAll(s1);
	}

	@Override
	public Set<T> next()
	{
		return m_iterator.next();
	}
	
	public static interface SolutionFilter<T>
	{
		public abstract boolean canAdd(Set<? extends T> set, T element);
	}
	
	protected static class PassthroughFilter<T> implements SolutionFilter<T>
	{

		@Override
		public boolean canAdd(Set<? extends T> set, T element) 
		{
			return true;
		}
		
	}
}

/*
    Automated fault localization in discrete structures
    Copyright (C) 2006-2017 Sylvain Hallé

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
package ca.uqac.lif.cornipickle.faultfinder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ca.uqac.lif.cornipickle.math.PowersetIterator;

public class FaultIterator<T> implements Iterator<Set<? extends Transformation<T>>>
{
	/**
	 * A string representing the version number of this library
	 */
	public static final String s_versionString = "0.2";
	
	private PowersetIterator<? extends Transformation<T>> m_transformationIterator;
	
	private Set<Set<? extends Transformation<T>>> m_currentSolutions;
	
	private final T m_object;
	
	private final Expression<T> m_expression;
	
	private Set<? extends Transformation<T>> m_nextSolution;
	
	private long m_solutionsExplored;
	
	private long m_validSolutions;
	
	private long m_maxSize;
	
	/**
	 * Whether the timeout has been exceeded
	 */
	private boolean m_timeoutExceeded;
	
	/**
	 * The start time of the last call to {@link #hasNext()}
	 */
	private long m_startTime;
	
	/**
	 * The maximum duration of each iteration of the iterator
	 */
	private long m_timeout;
	
	/**
	 * If set to true, the iterator will only enumerate solutions of
	 * the smallest cardinality found.
	 */
	private boolean m_justSmallest;
	
	public FaultIterator(Expression<T> expression, T object, PowersetIterator<? extends Transformation<T>> positivePowersetIterator)
	{
		super();
		m_object = object;
		m_expression = expression;
		m_currentSolutions = new HashSet<Set<? extends Transformation<T>>>();
		m_transformationIterator = positivePowersetIterator;
		m_maxSize = 0;
		m_startTime = 0;
		m_timeout = 0;
		m_timeoutExceeded = false;
		m_justSmallest = false;
		reset();
	}
	
	/**
	 * Sets whether the iterator limits itself to solutions of the smallest
	 * cardinality found.
	 * @param b Set to false to enumerate all solutions
	 * @return This iterator
	 */
	public FaultIterator<T> getJustSmallest(boolean b)
	{
		m_justSmallest = b;
		return this;
	}
	
	public FaultIterator<T> setMaxSize(long l)
	{
		m_maxSize = l;
		return this;
	}
	
	/**
	 * Sets the maximum time the iterator is allowed to search for a new
	 * solution. If this time is exceeded, the iterator will return false
	 * and no longer try to find solutions.
	 * @param ms The duration in ms
	 * @return This iterator
	 */
	public FaultIterator<T> setTimeout(long ms)
	{
		m_timeout = ms;
		m_transformationIterator.setTimeout(ms);
		return this;
	}

	@Override
	public boolean hasNext()
	{
		m_startTime = System.currentTimeMillis();
		if (m_timeoutExceeded)
		{
			return false;
		}
		if (!m_transformationIterator.hasNext())
		{
			return false;
		}
		while (m_transformationIterator.hasNext())
		{
			if (isTooLong())
				return false;
			Set<? extends Transformation<T>> current_candidate = m_transformationIterator.next();
			if (isTooLong())
				return false;
			int size = current_candidate.size();
			if (m_justSmallest && m_maxSize == 0)
			{
				m_maxSize = size;
			}
			if (m_maxSize > 0 &&  size > m_maxSize)
			{
				// Solution larger than upper bound: break
				return false;
			}
			m_solutionsExplored++;
			if (isSubsumed(current_candidate))
			{
				// Set of transformation includes an existing solution
				continue;
			}
			T result = applyAll(current_candidate);
			if (!m_expression.satisfies(result))
			{
				// Set of transformations does not fix original object
				continue;
			}
			// It works! Put this as next solution
			m_currentSolutions.add(current_candidate);
			m_nextSolution = current_candidate;
			m_validSolutions++;
			System.out.println(System.currentTimeMillis() - m_startTime);
			return true;
		}
		return false;
	}
	
	private boolean isTooLong()
	{
		long current_time = System.currentTimeMillis();
		if (m_timeout > 0 && current_time - m_startTime > m_timeout)
		{
			// This is taking too long: stop
			m_timeoutExceeded = true;
			return true;
		}
		return false;
	}

	@Override
	public Set<? extends Transformation<T>> next()
	{
		return m_nextSolution;
	}
	
	public FaultIterator<T> reset()
	{
		m_currentSolutions.clear();
		m_transformationIterator.reset();
		m_solutionsExplored = 0;
		m_validSolutions = 0;
		m_startTime = 0;
		m_timeoutExceeded = false;
		return this;
	}
	
	public long getSolutionsExplored()
	{
		return m_solutionsExplored;
	}
	
	public long getValidSolutions()
	{
		return m_validSolutions;
	}
	
	private boolean isSubsumed(Set<? extends Transformation<T>> candidate)
	{
		for (Set<? extends Transformation<T>> sol : m_currentSolutions)
		{
			if (candidate.containsAll(sol))
			{
				// A smaller solution was already found: discard
				return true;
			}
		}
		return false;
	}
	
	private T applyAll(Set<? extends Transformation<T>> candidate)
	{
		T out = m_object;
		for (Transformation<T> trf : candidate)
		{
			out = trf.apply(out);
		}
		return out;
	}
	
	public T getObject()
	{
		return m_object;
	}
	
	public static void main(String[] args)
	{
		System.out.println("FaultFinder v" + s_versionString + " - Fault localization in discrete structures");
		System.out.println("(C) 2016-2017 Sylvain Hallé, Laboratoire d'informatique formelle");
		System.out.println("Université du Québec à Chicoutimi, Canada");
		System.exit(0);
	}

}

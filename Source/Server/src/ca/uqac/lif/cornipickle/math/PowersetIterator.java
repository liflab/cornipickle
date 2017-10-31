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

import java.util.Iterator;
import java.util.Set;

/**
 * Enumerates elements of the power set of some set. This iterator
 * has two features:
 * <ol>
 * <li>It is iterative: that is, it does not generate the whole power
 *   set first, to iterate over it later on.</li>
 * <li>It enumerates sets by increasing cardinality.</li>
 * </ol>
 * @author Sylvain Hallé
 *
 * @param <T> The type of the elements from the original set
 */
public abstract class PowersetIterator<T> implements Iterator<Set<? extends T>>
{
	/**
	 * The elements to iterate over
	 */
	protected final Set<? extends T> m_elements;
	
	/**
	 * Whether the timeout has been exceeded
	 */
	protected boolean m_timeoutExceeded;
	
	/**
	 * The start time of the last call to {@link #hasNext()}
	 */
	protected long m_startTime;
	
	/**
	 * The maximum duration of each iteration of the iterator
	 */
	protected long m_timeout;
	
	public PowersetIterator(Set<? extends T> transformations)
	{
		super();
		m_elements = transformations;
	}
	
	public void reset()
	{
		m_startTime = 0;
		m_timeout = 0;
		m_timeoutExceeded = false;
	}
	
	protected boolean isTooLong()
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
	
	/**
	 * Sets the maximum time the iterator is allowed to search for a new
	 * solution. If this time is exceeded, the iterator will return false
	 * and no longer try to find solutions.
	 * @param ms The duration in ms
	 * @return This iterator
	 */
	public void setTimeout(long ms)
	{
		m_timeout = ms;
	}
	
}

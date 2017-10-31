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
package ca.uqac.lif.cornipickle.faultfinder.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;

public class FunctionTransformation implements Transformation<NamedDomain>
{
	protected Set<FunctionChange> m_changes;
	
	public FunctionTransformation()
	{
		super();
		m_changes = new HashSet<FunctionChange>();
	}

	@Override
	public NamedDomain apply(NamedDomain in)
	{
		for (FunctionChange c : m_changes)
		{
			in = c.apply(in);
		}
		return in;
	}
	
	@Override
	public String toString()
	{
		return m_changes.toString();
	}

	@Override
	public boolean conflictsWith(Transformation<NamedDomain> t)
	{
		FunctionTransformation pt = (FunctionTransformation) t;
		for (FunctionChange c1 : pt.m_changes)
		{
			for (FunctionChange c2 : m_changes)
			{
				if (c1.conflictsWith(c2))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public FunctionTransformation addChange(String name, boolean value, Object ... input)
	{
		m_changes.add(new FunctionChange(name, value, input));
		return this;
	}
	
	public static class FunctionChange
	{
		final String m_predicateName;
		
		final Object m_valueToSet;
		
		final List<Object> m_input;
		
		@SafeVarargs
		public FunctionChange(String name, Object value, Object ... input)
		{
			super();
			m_predicateName = name;
			m_valueToSet = value;
			m_input = Arrays.asList(input);
		}
		
		public NamedDomain apply(NamedDomain in)
		{
			NamedDomain nd = in.clone();
			RoteFunction p = (RoteFunction) nd.getFunction(m_predicateName);
			RoteFunction new_p = new RoteFunction(p);
			new_p.add(m_valueToSet, m_input);
			nd.addFunction(m_predicateName, new_p);
			return nd;
		}
		
		@Override
		public String toString()
		{
			return m_predicateName + m_input + "=" + m_valueToSet;
		}
		
		public boolean conflictsWith(FunctionChange c)
		{
			if (c.m_predicateName.compareTo(m_predicateName) == 0)
			{
				if (Arrays.deepEquals(m_input.toArray(), c.m_input.toArray()))
				{
					// Same predicate, same input: conflict
					return true;
				}
			}
			return false;			
		}
	}
}

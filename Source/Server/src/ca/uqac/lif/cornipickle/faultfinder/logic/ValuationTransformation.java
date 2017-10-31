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

import ca.uqac.lif.cornipickle.faultfinder.Transformation;


public class ValuationTransformation implements Transformation<PropositionalValuation> 
{
	private final String m_variableName;
	
	private final boolean m_valueToSet;
	
	public ValuationTransformation(String variable, boolean value)
	{
		super();
		m_variableName = variable;
		m_valueToSet = value;
	}

	@Override
	public PropositionalValuation apply(PropositionalValuation in)
	{
		PropositionalValuation out = in.getCopy();
		out.put(m_variableName, m_valueToSet);
		return out;
	}
	
	public String toString()
	{
		return m_variableName + "->" + m_valueToSet;
	}
	
	@Override
	public int hashCode()
	{
		return m_variableName.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof ValuationTransformation))
		{
			return false;
		}
		ValuationTransformation vt = (ValuationTransformation) o;
		return (vt.m_valueToSet == m_valueToSet && vt.m_variableName.compareTo(m_variableName) ==  0);
	}
	
	@Override
	public boolean conflictsWith(Transformation<PropositionalValuation> t)
	{
		if (!(t instanceof ValuationTransformation))
		{
			return false;
		}
		ValuationTransformation vt = (ValuationTransformation) t;
		if (vt.m_valueToSet != m_valueToSet && vt.m_variableName.compareTo(m_variableName) == 0)
		{
			return true;
		}
		return false;
	}

}

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

public class VariableSymbol implements Evaluable 
{
	public String m_variableName;
	
	public Object m_value = null;
	
	public VariableSymbol(String name)
	{
		super();
		m_variableName = name;
	}
	
	public VariableSymbol(VariableSymbol v)
	{
		this(v.m_variableName);
		m_value = v.m_value;
	}

	@Override
	public Object evaluate(Object in)
	{
		return m_value;
	}

	@Override
	public Evaluable getCopy()
	{
		return new VariableSymbol(this);
	}

	@Override
	public void replaceAll(String name, Object value)
	{
		if (name.compareTo(m_variableName) == 0)
		{
			m_value = value;
		}		
	}
	
	@Override
	public void accept(LogicalExpressionVisitor<?> visitor) 
	{
		visitor.visit(this);
	}
}

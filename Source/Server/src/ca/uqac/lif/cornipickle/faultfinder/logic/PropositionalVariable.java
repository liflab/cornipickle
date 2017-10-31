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

public class PropositionalVariable extends LogicalExpression<PropositionalValuation>
{
	private final String m_variableName;
	
	public PropositionalVariable(String name)
	{
		super();
		m_variableName = name;
	}
	
	public PropositionalVariable(PropositionalVariable v)
	{
		super();
		m_variableName = v.m_variableName;
	}

	@Override
	public boolean satisfies(PropositionalValuation in)
	{
		return in.get(m_variableName);
	}
	
	@Override
	public String toString()
	{
		return m_variableName;
	}
	
	@Override
	public PropositionalVariable getCopy()
	{
		return new PropositionalVariable(this);
	}

	@Override
	public void replaceAll(String name, Object value)
	{
		// Do nothing
	}

	@Override
	public void acceptPostfix(LogicalExpressionVisitor<PropositionalValuation> visitor)
	{
		visitor.visit(this);
	}
}

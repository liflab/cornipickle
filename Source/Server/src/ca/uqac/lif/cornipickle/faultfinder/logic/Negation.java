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

public class Negation<T> extends LogicalExpression<T>
{
	protected LogicalExpression<T> m_operand;
	
	public Negation(LogicalExpression<T> op)
	{
		super();
		m_operand = op;
	}
	
	public Negation(Negation<T> n)
	{
		super();
		m_operand = (LogicalExpression<T>) n.m_operand.getCopy();
	}

	@Override
	public boolean satisfies(T in)
	{
		return !m_operand.satisfies(in);
	}
	
	@Override
	public Negation<T> getCopy()
	{
		return new Negation<T>(this);
	}

	@Override
	public void replaceAll(String name, Object value)
	{
		m_operand.replaceAll(name, value);
	}
	
	@Override
	public void acceptPostfix(LogicalExpressionVisitor<T> visitor)
	{
		m_operand.acceptPostfix(visitor);
		visitor.visit(this);
	}
}

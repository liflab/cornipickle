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

public class Equals<T> extends LogicalExpression<T>
{
	Evaluable m_left;
	
	Evaluable m_right;
	
	public Equals(Evaluable left, Evaluable right)
	{
		super();
		m_left = left.getCopy();
		m_right = right.getCopy();
	}
	
	public Equals(Equals<T> e)
	{
		super();
		m_left = e.m_left.getCopy();
		m_right = e.m_right.getCopy();
	}
	
	@Override
	public boolean satisfies(Object in)
	{
		Object left = m_left.evaluate(in);
		Object right = m_right.evaluate(in);
		return left.equals(right);
	}
	
	@Override
	public Equals<T> getCopy()
	{
		return new Equals<T>(this);
	}

	@Override
	public void replaceAll(String name, Object value) 
	{
		m_left.replaceAll(name, value);
		m_right.replaceAll(name, value);
	}
	
	@Override
	public void acceptPostfix(LogicalExpressionVisitor<T> visitor)
	{
		m_left.accept(visitor);
		m_right.accept(visitor);
		visitor.visit(this);
	}
	
	@Override
	public String toString()
	{
		return m_left.toString() + " = " + m_right.toString();
	}
}

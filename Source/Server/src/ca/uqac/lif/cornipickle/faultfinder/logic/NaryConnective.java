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

import java.util.LinkedList;
import java.util.List;

public abstract class NaryConnective<T> extends LogicalExpression<T>
{
	protected List<LogicalExpression<T>> m_operands;
	
	public NaryConnective()
	{
		super();
		m_operands = new LinkedList<LogicalExpression<T>>();
	}
	
	public NaryConnective(NaryConnective<T> source)
	{
		this();
		for (LogicalExpression<T> e : source.m_operands)
		{
			m_operands.add((LogicalExpression<T>) e.getCopy());
		}
	}
	
	@SafeVarargs
	public NaryConnective(LogicalExpression<T> ... exps)
	{
		this();
		for (LogicalExpression<T> exp : exps)
		{
			addOperand(exp);
		}
	}
	
	public void addOperand(LogicalExpression<T> e)
	{
		m_operands.add(e);
	}

	@Override
	public abstract boolean satisfies(T in);
	
	public final void replaceAll(String name, Object value)
	{
		for (LogicalExpression<T> e : m_operands)
		{
			e.replaceAll(name, value);
		}
	}

	@Override
	public void acceptPostfix(LogicalExpressionVisitor<T> visitor)
	{
		for (LogicalExpression<T> exp : m_operands)
		{
			exp.acceptPostfix(visitor);
		}
		visitor.visit(this);
	}
}

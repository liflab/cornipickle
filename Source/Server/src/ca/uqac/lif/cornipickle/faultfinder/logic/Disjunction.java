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

public class Disjunction<T> extends NaryConnective<T>
{
	@SafeVarargs
	public Disjunction(LogicalExpression<T> ... exps)
	{
		super(exps);
	}
	
	public Disjunction(Disjunction<T> c)
	{
		super(c);
	}

	@Override
	public boolean satisfies(T in)
	{
		for (LogicalExpression<T> op : m_operands)
		{
			if (op.satisfies(in))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Disjunction<T> getCopy()
	{
		return new Disjunction<T>(this);
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (LogicalExpression<T> op : m_operands)
		{
			if (first)
				first = false;
			else
				out.append(" or ");
			out.append("(").append(op).append(")");
		}
		return out.toString();
	}

}

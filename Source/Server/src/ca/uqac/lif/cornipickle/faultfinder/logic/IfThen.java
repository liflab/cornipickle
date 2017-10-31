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

public class IfThen<T> extends NaryConnective<T>
{
	@SafeVarargs
	public IfThen(LogicalExpression<T> ... exps)
	{
		super(exps);
	}
	
	public IfThen(IfThen<T> c)
	{
		super(c);
	}

	@Override
	public boolean satisfies(T in)
	{
		if (m_operands.size() < 2)
			return false;
		LogicalExpression<T> left = m_operands.get(0);
		LogicalExpression<T> right = m_operands.get(1);
		if (left.satisfies(in))
		{
			return right.satisfies(in);
		}
		return true;
	}
	
	@Override
	public IfThen<T> getCopy()
	{
		return new IfThen<T>(this);
	}
	
	@Override
	public String toString()
	{
		return "if (" + m_operands.get(0) + ") then (" + m_operands.get(1) + ")";
	}

}

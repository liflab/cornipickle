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

public class ForAll extends Quantifier 
{
	
	public ForAll(String var_name, LogicalExpression<NamedDomain> operand, String domain)
	{
		super(var_name, operand, domain);
	}
	
	public ForAll(ForAll f)
	{
		super(f);
	}

	@Override
	public boolean satisfies(NamedDomain nd) 
	{
		for (Object value : nd.getSet(m_domainName))
		{
			LogicalExpression<NamedDomain> op_copy = (LogicalExpression<NamedDomain>) m_operand.getCopy();
			op_copy.replaceAll(m_variableName, value);
			if (!op_copy.satisfies(nd))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public LogicalExpression<NamedDomain> getCopy()
	{
		return new ForAll(this);
	}
	
	@Override
	public final void replaceAll(String name, Object value)
	{
		m_operand.replaceAll(name, value);
	}

}

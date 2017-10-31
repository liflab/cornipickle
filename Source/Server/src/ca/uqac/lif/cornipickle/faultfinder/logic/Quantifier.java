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

public abstract class Quantifier extends LogicalExpression<NamedDomain>
{
	protected final String m_variableName;
	
	protected final LogicalExpression<NamedDomain> m_operand;
	
	protected final String m_domainName;
	
	public Quantifier(String name, LogicalExpression<NamedDomain> operand, String domain)
	{
		super();
		m_variableName = name;
		m_operand = operand;
		m_domainName = domain;
	}
	
	public Quantifier(Quantifier q)
	{
		super();
		m_variableName = q.m_variableName;
		m_operand = (LogicalExpression<NamedDomain>) q.m_operand.getCopy();
		m_domainName = q.m_domainName;
	}
	
	@Override
	public void acceptPostfix(LogicalExpressionVisitor<NamedDomain> visitor)
	{
		m_operand.acceptPostfix(visitor);
		visitor.visit(this);
	}
}

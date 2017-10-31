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

public class FunctionVariable implements Evaluable
{
	protected final String m_predicateName;
	
	protected String[] m_operandNames;
	
	protected List<Object> m_operandValues;
	
	protected final int m_arity;
	
	public FunctionVariable(String name, String ... operand_names)
	{
		super();
		m_predicateName = name;
		m_arity = operand_names.length;
		m_operandNames = operand_names;
		m_operandValues = new LinkedList<Object>();
		for (int i = 0; i < m_arity; i++)
		{
			m_operandValues.add(null);
		}
	}
	
	public FunctionVariable(FunctionVariable in)
	{
		super();
		m_predicateName = in.m_predicateName;
		m_arity = in.m_arity;
		m_operandNames = in.m_operandNames;
		m_operandValues = new LinkedList<Object>();
		m_operandValues.addAll(in.m_operandValues);
	}
		
	public FunctionVariable setValue(String var_name, Object value)
	{
		for (int i = 0; i < m_operandNames.length; i++)
		{
			if (m_operandNames[i].compareTo(var_name) == 0)
			{
				m_operandValues.set(i, value);
			}
		}
		return this;
	}

	@Override
	public Object evaluate(Object in)
	{
		Function f = ((ConcreteNamedDomain) in).getFunction(m_predicateName);
		return f.evaluate(m_operandValues);
	}
	
	@Override
	public FunctionVariable getCopy()
	{
		return new FunctionVariable(this);
	}

	@Override
	public void replaceAll(String name, Object value)
	{
		setValue(name, value);		
	}

	@Override
	public void accept(LogicalExpressionVisitor<?> visitor) 
	{
		visitor.visit(this);
	}

}

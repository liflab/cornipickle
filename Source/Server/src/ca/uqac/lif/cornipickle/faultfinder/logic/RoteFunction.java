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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoteFunction extends Function
{
	private Map<List<Object>,Object> m_definition;
	
	private final int m_arity;
	
	public RoteFunction(int arity)
	{
		super();
		m_arity = arity;
		m_definition = new HashMap<List<Object>,Object>();
	}

	/**
	 * Initializes a function, by specifying the inputs and the output
	 * values they map to. There are several things to look out:
	 * <ul>
	 * <li>Make sure that the length of each line of the input array is the
	 *   same as <code>arity</code>
	 * </li>
	 * @param arity The arity of the predicate
	 * @param inputs An array, where each line is itself an array of values
	 *   representing one input
	 */
	public RoteFunction(int arity, Object[][] inputs, Object[] values)
	{
		this(arity);
		for (int i = 0; i < Math.min(inputs.length, values.length); i++)
		{
			Object[] input = inputs[i];
			Object value = values[i];
			add(value, input);
		}
	}
	
	public RoteFunction(RoteFunction p)
	{
		this(p.m_arity);
		m_definition.putAll(p.m_definition);
	}
	
	public RoteFunction add(Object value, List<Object> v)
	{
		m_definition.put(v, value);
		return this;
	}
	
	public RoteFunction add(Object value, Object ... v)
	{
		List<Object> inputs = Arrays.asList(v);
		return add(value, inputs);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object evaluate(Object o)
	{
		if (!(o instanceof List))
		{
			return null;
		}
		return m_definition.get((List<Object>) o);
	}
	
	@Override
	public int getArity()
	{
		return m_arity;
	}

	@Override
	public Evaluable getCopy()
	{
		return this;
	}

	@Override
	public void replaceAll(String name, Object value)
	{
		// Do nothing	
	}

	@Override
	public void accept(LogicalExpressionVisitor<?> visitor) 
	{
		visitor.visit(this);
	}
}

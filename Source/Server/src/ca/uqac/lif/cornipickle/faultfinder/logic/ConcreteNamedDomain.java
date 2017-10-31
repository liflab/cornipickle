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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConcreteNamedDomain implements NamedDomain
{
	protected Map<String,Set<Object>> m_sets;
	
	protected Map<String,Function> m_functions;
	
	public ConcreteNamedDomain()
	{
		super();
		m_sets = new HashMap<String,Set<Object>>();
		m_functions = new HashMap<String,Function>();
	}
	
	public ConcreteNamedDomain(ConcreteNamedDomain nd)
	{
		this();
		m_sets.putAll(nd.m_sets);
		m_functions.putAll(nd.m_functions);
	}
	
	@Override
	public Function getFunction(String name)
	{
		return m_functions.get(name);
	}
	
	@Override
	public Function addFunction(String name, Function f)
	{
		return m_functions.put(name, f);
	}
	
	@Override
	public Set<Object> getSet(String name)
	{
		return m_sets.get(name);
	}
	
	@Override
	public Set<Object> addSet(String name, Set<Object> s)
	{
		return m_sets.put(name, s);
	}
	
	@Override
	public ConcreteNamedDomain clone()
	{
		return new ConcreteNamedDomain(this);
	}

}

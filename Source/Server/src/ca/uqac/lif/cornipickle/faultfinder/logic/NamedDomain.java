package ca.uqac.lif.cornipickle.faultfinder.logic;

import java.util.Set;

public interface NamedDomain
{
	public Function getFunction(String name);
	
	public Set<Object> getSet(String name);
	
	public Function addFunction(String name, Function f);
	
	public Set<Object> addSet(String name, Set<Object> s);
	
	public NamedDomain clone();
}

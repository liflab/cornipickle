package ca.uqac.lif.cornipickle.faultfinder;

import java.util.Set;

import ca.uqac.lif.cornipickle.math.NegativePowersetIterator;
import ca.uqac.lif.cornipickle.math.NegativePowersetIterator.ElementFilter;

public class NegativeFaultIterator<T> extends FaultIterator<T>
{
	public NegativeFaultIterator(Expression<T> expression, T object, Set<? extends Transformation<T>> transformations, ElementFilter<Transformation<T>> filter) 
	{
		super(expression, object, new NegativePowersetIterator<Transformation<T>>(transformations, filter));
	}

}

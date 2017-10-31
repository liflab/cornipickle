package ca.uqac.lif.cornipickle.faultfinder;

import java.util.Set;

import ca.uqac.lif.cornipickle.math.PositivePowersetIterator;
import ca.uqac.lif.cornipickle.math.PositivePowersetIterator.SolutionFilter;

public class PositiveFaultIterator<T> extends FaultIterator<T>
{
	public PositiveFaultIterator(Expression<T> expression, T object, Set<? extends Transformation<T>> transformations, SolutionFilter<Transformation<T>> filter) 
	{
		super(expression, object, new PositivePowersetIterator<Transformation<T>>(transformations, filter));
	}

}

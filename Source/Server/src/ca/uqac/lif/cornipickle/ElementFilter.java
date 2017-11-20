package ca.uqac.lif.cornipickle;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.cornipickle.math.NegativePowersetIterator;
import ca.uqac.lif.cornipickle.math.PositivePowersetIterator;
import ca.uqac.lif.json.JsonElement;

public class ElementFilter implements NegativePowersetIterator.ElementFilter<Transformation<JsonElement>>, PositivePowersetIterator.SolutionFilter<Transformation<JsonElement>>
{

	@Override
	public boolean canAdd(Set<? extends Transformation<JsonElement>> set, Transformation<JsonElement> element) {
		for (Transformation<JsonElement> t : set)
		{
			if (t.conflictsWith(element))
				return false;
		}
		return true;
	}

	@Override
	public void filter(Collection<? extends Transformation<JsonElement>> elements,
			Transformation<JsonElement> element) 
	{
		Iterator<? extends Transformation<JsonElement>> it = elements.iterator();
		while (it.hasNext())
		{
			Transformation<JsonElement> pt = it.next();
			if (pt.conflictsWith(element))
			{
				it.remove();
			}
		}
		
	}

}

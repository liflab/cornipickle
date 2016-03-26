package ca.uqac.lif.cornipickle.fiddle;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;

public abstract class FiddleOperation
{
	public FiddleOperation()
	{
		super();
	}
	
	public abstract boolean fire(JsonMap in);
	
	public abstract JsonElement doOperation(JsonMap argument, Interpreter i);
}

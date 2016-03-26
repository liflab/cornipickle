package ca.uqac.lif.cornipickle.fiddle;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.json.JsonTrue;

public class SetProperty extends FiddleOperation 
{
	public SetProperty()
	{
		super();
	}

	@Override
	public boolean fire(JsonMap in) 
	{
		String action = ((JsonString) in.get("action")).stringValue();
		return action.compareToIgnoreCase("set-property") == 0;
	}

	@Override
	public JsonElement doOperation(JsonMap argument, Interpreter i)
	{
		String property = ((JsonString) argument.get("property")).stringValue();
		try 
		{
			i.parseProperties(property);
		} 
		catch (ParseException e) 
		{
			return new JsonFalse();
		}
		return new JsonTrue();
	}

}

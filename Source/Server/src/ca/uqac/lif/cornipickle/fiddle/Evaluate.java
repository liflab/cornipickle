package ca.uqac.lif.cornipickle.fiddle;

import java.util.Map;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.azrael.json.JsonSerializer;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;

public class Evaluate extends FiddleOperation 
{
	public Evaluate()
	{
		super();
	}

	@Override
	public boolean fire(JsonMap in) 
	{
		String action = ((JsonString) in.get("action")).stringValue();
		return action.compareToIgnoreCase("evaluate") == 0;
	}

	@Override
	public JsonElement doOperation(JsonMap argument, Interpreter i)
	{
		JsonElement contents = argument.get("contents");
		i.evaluateAll(contents);
		JsonSerializer j_ser = new JsonSerializer();
		j_ser.addClassLoader(Interpreter.class.getClassLoader());
		Map<StatementMetadata,Verdict> verdicts = i.getVerdicts();
		try 
		{
			JsonElement je = j_ser.serialize(verdicts);
			return je;
		} 
		catch (SerializerException e) 
		{
			return new JsonFalse();
		}
	}

}

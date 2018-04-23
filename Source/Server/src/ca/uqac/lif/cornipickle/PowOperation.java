package ca.uqac.lif.cornipickle;


import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class PowOperation extends Operation {

	PowOperation()
	{
		super();
	}

	public PowOperation(Property p1, Property p2)
	{
		super(p1, p2);
	}
	
	@Override
	public JsonElement evaluate(JsonElement t, Map<String, JsonElement> d)
	{
		JsonNumber left = (JsonNumber) m_left.evaluate(t, d);
		JsonNumber right = (JsonNumber) m_right.evaluate(t, d);
		Number intLeft = left.numberValue();
		Number intRight = right.numberValue();
		int ret = (int) Math.pow(intRight.intValue(),1.0/intLeft.intValue());
		return new JsonNumber(new Integer(ret));
	}

	@Override
	public String toString(String indent)
	{
		StringBuilder out = new StringBuilder();
		out.append(m_left.toString()).append(" ^ ").append(m_right.toString());
		return out.toString();
	}

	@Override
	public AddOperation getClone()
	{
		AddOperation out = new AddOperation(m_left, m_right);
		return out;
	}
	

}
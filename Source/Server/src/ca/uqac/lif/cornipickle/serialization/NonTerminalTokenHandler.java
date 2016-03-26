package ca.uqac.lif.cornipickle.serialization;

import ca.uqac.lif.azrael.Handler;
import ca.uqac.lif.azrael.Serializer;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.bullwinkle.NonTerminalToken;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonString;

public class NonTerminalTokenHandler extends Handler<JsonElement>
{

	public NonTerminalTokenHandler(Serializer<JsonElement> s)
	{
		super(s);
	}

	@Override
	public boolean appliesTo(Class<?> clazz)
	{
		return clazz != null && clazz.equals(NonTerminalToken.class);
	}

	@Override
	public JsonElement serializeAs(Object o, Class<?> clazz) throws SerializerException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deserializeAs(JsonElement e, Class<?> clazz)	throws SerializerException
	{
		if (e == null || !(e instanceof JsonString))
		{
			return null;
		}
		assert e instanceof JsonString;
		JsonString s = (JsonString) e;
		return new NonTerminalToken(s.stringValue());
	}

}

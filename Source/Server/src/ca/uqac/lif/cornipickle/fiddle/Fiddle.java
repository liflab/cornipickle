/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cornipickle.fiddle;

import java.util.LinkedList;
import java.util.List;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.serialization.CornipickleSerializer;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

/**
 * A gateway to the Cornipickle interpreter that allows a completely
 * stateless interaction between calls.
 * <p>
 * One interacts with the "fiddle" through the
 * {@link #doOperation(String, String)} method.
 * @author sylvain
 *
 */
public class Fiddle
{
	protected CornipickleSerializer m_serializer = new CornipickleSerializer();

	protected JsonParser m_parser = new JsonParser();

	protected List<FiddleOperation> m_operations;

	public Fiddle()
	{
		super();
		m_operations = new LinkedList<FiddleOperation>();
		addOperation(new SetProperty());
		addOperation(new Evaluate());
	}

	protected void addOperation(FiddleOperation op)
	{
		m_operations.add(op);
	}

	/**
	 * Performs an interaction with the fiddle.
	 * @param state The state of the Cornipickle interpreter before doing
	 *   the interaction
	 * @param argument A JSON string containing the arguments
	 *   corresponding to the interaction. See the documentation of each
	 *   {@link FiddleOperation} for details.
	 * @return The FiddlePair corresponding to the result of the operation
	 */
	public FiddlePair doOperation(String state, String argument)
	{
		Interpreter i = null;
		JsonElement je = null;
		try
		{
			je = m_parser.parse(argument);
			if (!(je instanceof JsonMap))
			{
				return null;
			}
			if (state.isEmpty())
			{
				// If empty state is provided, create a blank interpreter
				i = new Interpreter();
			}
			else
			{
				// Otherwise, deserialize interpreter from string
				i = m_serializer.deserializeAs(state, Interpreter.class);
			}
			for (FiddleOperation op : m_operations)
			{
				if (!(op.fire((JsonMap) je)))
				{
					continue;
				}
				JsonElement je_out = op.doOperation((JsonMap) je, i);
				String json_state_out = m_serializer.serialize(i);
				return new FiddlePair(json_state_out, je_out.toString());
			}
		}
		catch (SerializerException e)
		{
			return null;
		} 
		catch (JsonParseException e)
		{
			return null;
		}
		// If no operation succeeded, return null
		return null;
	}
}

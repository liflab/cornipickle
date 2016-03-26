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

/**
 * Fiddle operation to evaluate a new property to the interpreter.
 * <p>
 * The format of the request is as follows:
 * <pre>
 * {
 *   "action"   : "evaluate",
 *   "contents" : { ... }
 * }
 * </pre>
 * where the content of the <tt>contents</tt> attribute is the JSON
 * structure produced by the JavaScript probe.
 * <p>
 * The format of the response is the JSON serialization of all the
 * verdicts of the properties in the interpreter.
 * 
 * @author sylvain
 *
 */
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

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

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.json.JsonTrue;

/**
 * Fiddle operation to add a new property to the interpreter.
 * <p>
 * The format of the request is as follows:
 * <pre>
 * {
 *   "action"   : "set-property",
 *   "property" : "..."
 * }
 * </pre>
 * where the content of the <tt>contents</tt> attribute is a string
 * of valid Cornipickle expressions.
 * <p>
 * The format of the response is either the JSON value <tt>true</tt>
 * if the operation succeeded, or <tt>false</tt> otherwise.
 * 
 * @author sylvain
 *
 */
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

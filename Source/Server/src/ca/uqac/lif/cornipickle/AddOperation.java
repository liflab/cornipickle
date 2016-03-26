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
package ca.uqac.lif.cornipickle;

import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class AddOperation extends Operation
{
	AddOperation()
	{
		super();
	}

	public AddOperation(Property p1, Property p2)
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
		int ret = intLeft.intValue() + intRight.intValue();
		return new JsonNumber(new Integer(ret));
	}

	@Override
	public String toString(String indent)
	{
		StringBuilder out = new StringBuilder();
		out.append(m_left.toString()).append(" + ").append(m_right.toString());
		return out.toString();
	}

	@Override
	public AddOperation getClone()
	{
		AddOperation out = new AddOperation(m_left, m_right);
		return out;
	}

}

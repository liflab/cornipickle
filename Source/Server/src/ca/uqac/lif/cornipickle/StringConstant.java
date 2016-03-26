/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015 Sylvain Hallé

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
import ca.uqac.lif.json.JsonString;

public class StringConstant extends Constant
{
	protected JsonString m_value;

	/**
	 * Empty constructor, added only to simplify serialization
	 */
	StringConstant()
	{
		super();
		m_value = null;
	}

	public StringConstant(String s)
	{
		super();
		m_value = new JsonString(s);
	}

	public StringConstant(JsonString s)
	{
		super();
		m_value = s;
	}

	@Override
	public JsonElement evaluate(JsonElement t, Map<String, JsonElement> d)
	{
		return m_value;
	}

	@Override
	public String toString(String indent)
	{
		return m_value.stringValue();
	}

}

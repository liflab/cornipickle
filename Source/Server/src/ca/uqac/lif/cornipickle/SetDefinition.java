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

import java.util.List;
import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;

public class SetDefinition extends SetExpression
{
	protected StringConstant m_setName;

	/**
	 * Empty constructor, added only to simplify serialization
	 */
	SetDefinition()
	{
		super();
	}

	public SetDefinition(StringConstant name)
	{
		super();
		m_setName = name;
	}

	public SetDefinition(String name)
	{
		this(new StringConstant(name));
	}

	public String getName()
	{
		return m_setName.toString();
	}

	@Override
	public List<JsonElement> evaluate(JsonElement t, Map<String, JsonElement> d)
	{
		JsonElement e = d.get(m_setName.toString());
		if (!(e instanceof JsonList))
		{
			return null;
		}
		return (JsonList) e;
	}

	@Override
	public String toString(String indent)
	{
		return m_setName.toString();
	}

	@Override
	public SetExpression getClone()
	{
		SetDefinition out = new SetDefinition(m_setName);
		return out;
	}
}
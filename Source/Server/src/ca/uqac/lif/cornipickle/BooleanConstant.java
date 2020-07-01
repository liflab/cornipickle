/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2020 Sylvain Hallé

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

public class BooleanConstant extends Statement
{
	protected Verdict m_value;

	BooleanConstant()
	{
		super();
		m_value = null;
	}
	
	public BooleanConstant(Boolean b)
	{
		super();
		if (b)
		{
			m_value = new Verdict(Verdict.Value.TRUE);
		}
		else
		{
			m_value = new Verdict(Verdict.Value.FALSE);
		}
	}

	public BooleanConstant(String s)
	{
		super();
		if (s.compareTo("1") == 0 || s.compareToIgnoreCase("true") == 0)
		{
			m_value = new Verdict(Verdict.Value.TRUE);
		}
		m_value = new Verdict(Verdict.Value.FALSE);
	}

	@Override
	public String toString(String indent)
	{
		return m_value.toString();
	}

	@Override
	public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
	{
		return m_value;
	}

	@Override
	public boolean isTemporal()
	{
		return false;
	}

	@Override
	public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
	{
		return m_value;
	}

	@Override
	public Statement getClone() 
	{
		return this;
	}

	@Override
	public void resetHistory()
	{
		// Nothing to do
	}

	@Override
	public void prefixAccept(LanguageElementVisitor visitor)
	{
		visitor.visit(this);
	    visitor.pop();
	}

	@Override
	public void postfixAccept(LanguageElementVisitor visitor)
	{
		visitor.visit(this);
	    visitor.pop();
	}
}

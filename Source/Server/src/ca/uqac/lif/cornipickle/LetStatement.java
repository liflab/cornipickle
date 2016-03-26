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

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.json.JsonElement;

public class LetStatement extends Statement
{
	protected String m_variableName;

	protected Property m_property;

	protected JsonElement m_value;

	protected Statement m_innerStatement;

	LetStatement()
	{
		this("", null);
	}

	public LetStatement(String variableName, Property p)
	{
		super();
		m_value = null;
		m_property = p;
		m_variableName = variableName;
	}

	public LetStatement(StringConstant variableName, Property p)
	{
		this(variableName.toString(), p);
	}

	public String getVariable()
	{
		return m_variableName;
	}

	@Override
	public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
	{
		if (m_value == null)
		{
			m_value = m_property.evaluate(j, d);
		}
		Map<String, JsonElement> new_d = new HashMap<String, JsonElement>();
		new_d.putAll(d);
		new_d.put(m_variableName, m_value);
		return m_innerStatement.evaluateAtemporal(j, new_d);
	}

	@Override
	public boolean isTemporal()
	{
		return false;
	}

	@Override
	public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
	{
		if (m_value == null)
		{
			m_value = m_property.evaluate(j, d);
		}
		Map<String, JsonElement> new_d = new HashMap<String, JsonElement>();
		new_d.putAll(d);
		new_d.put(m_variableName, m_value);
		return m_innerStatement.evaluateTemporal(j, new_d);
	}

	public void setStatement(Statement s)
	{
		m_innerStatement = s;
	}

	@Override
	public Statement getClone()
	{
		LetStatement out = new LetStatement(m_variableName, m_property);
		out.m_innerStatement = m_innerStatement.getClone();
		return out;
	}

	@Override
	public void resetHistory()
	{
		m_innerStatement.resetHistory();
		m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
	}

	@Override
	public String toString(String indent)
	{
		StringBuilder out = new StringBuilder();
		out.append(indent).append("let ").append(m_variableName);
		out.append(" be ").append(m_property.toString());
		out.append(" (\n").append(m_innerStatement.toString(indent + "  "));
		out.append("\n").append(indent).append(")");
		return out.toString();
	}

	@Override
	public void prefixAccept(LanguageElementVisitor visitor)
	{
		visitor.visit(this);
		m_property.prefixAccept(visitor);
		m_innerStatement.prefixAccept(visitor);
		visitor.pop();
	}

	@Override
	public void postfixAccept(LanguageElementVisitor visitor)
	{
		m_property.postfixAccept(visitor);
		m_innerStatement.postfixAccept(visitor);
		visitor.visit(this);
		visitor.pop();
	}

}

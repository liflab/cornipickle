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
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;

public class WhenIsNow extends Statement
{
	/**
	 * The inner statement
	 */
	protected Statement m_innerStatement;

	/**
	 * The variable name before
	 */
	protected String m_variableBefore;

	/**
	 * The variable name after
	 */
	protected String m_variableAfter;

	/**
	 * The element now
	 */
	protected JsonElement m_elementNow;

	/**
	 * Empty constructor, added only to simplify serialization
	 */
	WhenIsNow()
	{
		super();
	}

	public WhenIsNow(String before, String after)
	{
		super();
		m_variableBefore = before;
		m_variableAfter = after;
		m_elementNow = null;
	}

	public WhenIsNow(StringConstant before, StringConstant after)
	{
		this(before.toString(), after.toString());
	}

	public String getVariableBefore()
	{
		return m_variableBefore;
	}

	public String getVariableAfter()
	{
		return m_variableAfter;
	}

	public void setStatement(Statement s)
	{
		m_innerStatement = s;
	}

	@Override
	public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
	{
		if (m_elementNow == null)
		{
			// Fetch element
			JsonElement reference = d.get(m_variableBefore);
			// Get its cornipickleid
			if (reference == null || !(reference instanceof JsonMap))
			{
				// Not supposed to happen
				assert false;
			}
			JsonMap reference_m = (JsonMap) reference;
			JsonElement e_id = reference_m.get("cornipickleid");
			if (e_id instanceof JsonNumber)
			{
				Number n_id = ((JsonNumber) e_id).numberValue();
				m_elementNow = fetchWithId(j, n_id);
			}
		}
		Map<String, JsonElement> new_d = new HashMap<String, JsonElement>();
		new_d.putAll(d);
		new_d.put(m_variableAfter, m_elementNow);
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
		if (m_elementNow == null)
		{
			// Fetch element
			JsonElement reference = d.get(m_variableBefore);
			// Get its cornipickleid
			if (reference == null || !(reference instanceof JsonMap))
			{
				// Not supposed to happen
				assert false;
			}
			JsonMap reference_m = (JsonMap) reference;
			JsonElement e_id = reference_m.get("cornipickleid");
			if (e_id instanceof JsonNumber)
			{
				Number n_id = ((JsonNumber) e_id).numberValue();
				m_elementNow = fetchWithId(j, n_id);
			}
		}
		Map<String, JsonElement> new_d = new HashMap<String, JsonElement>();
		new_d.putAll(d);
		new_d.put(m_variableAfter, m_elementNow);
		return m_innerStatement.evaluateTemporal(j, new_d);
	}

	@Override
	public Statement getClone()
	{
		WhenIsNow out = new WhenIsNow(m_variableBefore, m_variableAfter);
		out.m_innerStatement = m_innerStatement.getClone();
		return out;
	}

	@Override
	public void resetHistory()
	{
		m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
		m_elementNow = null;
		m_innerStatement.resetHistory();
	}

	@Override
	public String toString(String indent)
	{
		StringBuilder out = new StringBuilder();
		out.append(indent).append("When ").append(m_variableBefore).append(" is now ");
		out.append(m_variableAfter).append(" (\n");
		out.append(m_innerStatement.toString(indent + "  "));
		out.append(")");
		return out.toString();
	}

	@Override
	public void prefixAccept(LanguageElementVisitor visitor)
	{
		visitor.visit(this);
		m_innerStatement.prefixAccept(visitor);
		visitor.pop();
	}

	@Override
	public void postfixAccept(LanguageElementVisitor visitor)
	{
		m_innerStatement.postfixAccept(visitor);
		visitor.visit(this);
		visitor.pop();
	}

	/**
	 * Fetches an element with a given value of parameter cornipickleid
	 * @param e The root of the tree to search
	 * @param id The cornipickleid to look for
	 * @return A single element with given cornipickle id, or null if
	 *   not found
	 */
	protected static JsonElement fetchWithId(JsonElement e, Number id)
	{
		if (!(e instanceof JsonMap))
		{
			return null;
		}
		JsonMap m = (JsonMap) e;
		// Get element's cornipickleid
		JsonElement e_id = m.get("cornipickleid");
		if (e_id != null && e_id instanceof JsonNumber)
		{
			JsonNumber c_id = (JsonNumber) e_id; 
			if (c_id != null && c_id.numberValue().intValue() == id.intValue())
			{
				// This is the id we are looking for; return the element
				return m;
			}
		}
		JsonElement e_list = m.get("children");
		if (!(e_list instanceof JsonList))
		{
			return null;
		}
		JsonList l_list = (JsonList) e_list;
		// Otherwise, iterate over children
		for (JsonElement l_list_item : l_list)
		{
			JsonElement out = fetchWithId(l_list_item, id);
			if (out != null)
			{
				// One of them contains the element we want: return it
				return out;
			}
		}
		// Sorry, no success
		return null;
	}

}

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

public class IsDefinedStatement extends Statement
{
	protected Property m_property;

	IsDefinedStatement()
	{
		super();
	}

	public final Verdict evaluateTemporal(JsonElement j, Map<String,JsonElement> d)
	{
		JsonElement p = m_property.evaluate(j, d);
		m_verdict = check(p);
		return m_verdict;
	}

	@Override
	public final Verdict evaluateAtemporal(JsonElement j, Map<String,JsonElement> d)
	{
		JsonElement p = m_property.evaluate(j, d);
		return check(p);
	}

	public final boolean isTemporal()
	{
		return false;
	}

	public void resetHistory()
	{
		m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
	}

	public void setProperty(final Property p)
	{
		m_property = p;
	}

	protected Verdict check(JsonElement p)
	{
		JsonString js = (JsonString) p;
		Verdict out = new Verdict();
		Witness w = new Witness();
		w.add(new Witness(js));
		//javascript returns undefined if proprety is not found, for string and number values
		if (js.stringValue().equals("undefined"))
		{
			out.setValue(Verdict.Value.FALSE);
			out.setWitnessFalse(w);
		}
		else
		{
			out.setValue(Verdict.Value.TRUE);
			out.setWitnessTrue(w); 
		}
		return out;
	}


	@Override
	public String toString(String indent)
	{
		StringBuilder out = new StringBuilder();
		out.append(m_property).append(" is defined");
		return out.toString();
	}

	public String getKeyword()
	{
		return "is defined";
	}

	@Override
	public final void postfixAccept(LanguageElementVisitor visitor)
	{
		m_property.postfixAccept(visitor);
		visitor.visit(this);
		visitor.pop();
	}

	@Override
	public final void prefixAccept(LanguageElementVisitor visitor)
	{
		visitor.visit(this);
		m_property.prefixAccept(visitor);
		visitor.pop();
	}

	@Override
	public IsDefinedStatement getClone()
	{
		IsDefinedStatement out = new IsDefinedStatement();
		out.m_property = m_property.getClone();
		return out;
	}
}

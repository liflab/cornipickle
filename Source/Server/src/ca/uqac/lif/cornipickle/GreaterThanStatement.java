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

import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;

public class GreaterThanStatement extends ComparisonStatement
{
	GreaterThanStatement()
	{
		super();
	}

	@Override
	protected Verdict compare(JsonString e1, JsonString e2)
	{
		// If both values are actually numbers, we compare them as numbers
		if (JsonNumber.isNumeric(e1) && JsonNumber.isNumeric(e2))
		{
			JsonNumber n1 = new JsonNumber(Integer.parseInt(e1.stringValue()));
			JsonNumber n2 = new JsonNumber(Integer.parseInt(e2.stringValue()));
			return compare(n1, n2);
		}
		Verdict out = new Verdict();
		Witness w = new Witness();
		w.add(new Witness(e1));
		w.add(new Witness(e2));
		if (e1.stringValue().compareTo(e2.stringValue()) == 0)
		{
			out.setValue(Verdict.Value.TRUE);
			out.setWitnessTrue(w);
		}
		else
		{
			out.setValue(Verdict.Value.FALSE);
			out.setWitnessFalse(w);
		}
		return out;
	}

	@Override
	protected Verdict compare(JsonNumber e1, JsonNumber e2)
	{
		Verdict out = new Verdict();
		Witness w = new Witness();
		w.add(new Witness(e1));
		w.add(new Witness(e2));
		if (e1.numberValue().floatValue() > e2.numberValue().floatValue())
		{
			out.setValue(Verdict.Value.TRUE);
			out.setWitnessTrue(w);
		}
		else
		{
			out.setValue(Verdict.Value.FALSE);
			out.setWitnessFalse(w);
		}
		return out;
	}

	@Override
	public String toString(String indent)
	{
		StringBuilder out = new StringBuilder();
		out.append(m_left).append(" is greater than ").append(m_right);
		return out.toString();
	}

	@Override
	public String getKeyword()
	{
		return "is greater than";
	}

	@Override
	public GreaterThanStatement getClone()
	{
		GreaterThanStatement out = new GreaterThanStatement();
		out.m_left = m_left.getClone();
		out.m_right = m_right.getClone();
		return out;
	}

}

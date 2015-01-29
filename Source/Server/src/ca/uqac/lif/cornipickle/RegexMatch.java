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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.cornipickle.json.JsonNumber;
import ca.uqac.lif.cornipickle.json.JsonString;

public class RegexMatch extends ComparisonStatement
{

	@Override
	protected Verdict compare(JsonString e1, JsonString e2)
	{
		String subject = e1.stringValue();
		String pattern = e2.stringValue();
		Pattern pat = Pattern.compile(pattern);
		Matcher mat = pat.matcher(subject);
		if (mat.find())
		{
			return Verdict.TRUE;
		}
		return Verdict.FALSE;
	}

	@Override
	protected Verdict compare(JsonNumber e1, JsonNumber e2)
	{
		// Regexes don't apply to numbers
		return Verdict.FALSE;
	}

	@Override
	public String getKeyword()
	{
		return "matches";
	}

	@Override
	public String toString(String indent)
	{
    StringBuilder out = new StringBuilder();
    out.append(m_left).append(" matches ").append(m_right);
    return out.toString();
	}

}

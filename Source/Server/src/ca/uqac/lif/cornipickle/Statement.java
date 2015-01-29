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

import ca.uqac.lif.cornipickle.json.JsonElement;

public abstract class Statement extends LanguageElement
{ 
  public static enum Verdict {TRUE, FALSE, INCONCLUSIVE};

  protected Statement.Verdict m_verdict;
  
  public Statement()
  {
    m_verdict = Verdict.INCONCLUSIVE;
  }

  public final Verdict evaluate(JsonElement j)
  {
    Map<String,JsonElement> d = new HashMap<String,JsonElement>();
    return evaluate(j, d);
  }

  public abstract Verdict evaluate(JsonElement j, Map<String,JsonElement> d);

  public static final Verdict threeValuedAnd(Verdict x, Verdict y)
  {
    if (x == Verdict.FALSE || y == Verdict.FALSE)
    {
      return Verdict.FALSE;
    }
    if (x == Verdict.TRUE && y == Verdict.TRUE)
    {
      return Verdict.TRUE;
    }
    return Verdict.INCONCLUSIVE;
  }

  public abstract Statement getClone();

  public void resetHistory()
  {
    // Nothing to do for non-temporal statements
    return;
  }

  public static final Verdict threeValuedOr(Verdict x, Verdict y)
  {
    if (x == Verdict.TRUE || y == Verdict.TRUE)
    {
      return Verdict.TRUE;
    }
    if (x == Verdict.FALSE && y == Verdict.FALSE)
    {
      return Verdict.FALSE;
    }
    return Verdict.INCONCLUSIVE;
  }

  public static final Verdict threeValuedNot(Verdict x)
  {
    if (x == Verdict.FALSE)
    {
      return Verdict.TRUE;
    }
    if (x == Verdict.TRUE)
    {
      return Verdict.FALSE;
    }
    return Verdict.INCONCLUSIVE;
  }
}

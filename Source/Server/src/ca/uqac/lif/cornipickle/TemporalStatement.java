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

public abstract class TemporalStatement extends Statement
{
  // Force descendants to override Statement's getClone()
  public abstract Statement getClone();

  // Force descendants to override Statement's resetHistory()
  public abstract void resetHistory();

  @Override
  public final boolean isTemporal()
  {
    return true;
  }
  
  @Override
  public final Verdict evaluateAtemporal(JsonElement j, Map<String,JsonElement> d)
  {
    // Not supposed to happen!
    System.err.println("Attempting to evaluate temporal statement with atemporal");
    return null;
  }
}

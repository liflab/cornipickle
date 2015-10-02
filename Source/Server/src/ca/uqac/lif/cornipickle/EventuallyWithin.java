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

import java.util.Iterator;
import java.util.Map;

import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonNumber;

public class EventuallyWithin extends Globally
{
  protected NumberConstant m_timestamp;
  protected long future_timestamp;

  public EventuallyWithin(NumberConstant timestamp)
  {
    m_timestamp = timestamp;
    JsonNumber eval_timestamp = (JsonNumber) m_timestamp.evaluate(null, null);
    future_timestamp = java.lang.System.currentTimeMillis() + eval_timestamp.numberValue().longValue() * 1000;
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  { 
    long currentTime = java.lang.System.currentTimeMillis();
    System.out.println("Target time: " + future_timestamp); //debug
    System.out.println("Current time: " + currentTime); //debug
    // Instantiate new inner statement
    Statement new_s = m_innerStatement.getClone();
    m_inMonitors.add(new_s);
    // Evaluate each
    Iterator<Statement> it = m_inMonitors.iterator();
    while (it.hasNext())
    {
      Statement st = it.next();
      Verdict st_v = st.evaluate(j, d);
      if (st_v.is(Verdict.Value.FALSE))
      {
        System.out.println("Eventually within: Verdict is inconclusive or false.");
        //it.remove(); //to see if removing this causes problems with multiple statements
      }
      if (st_v.is(Verdict.Value.TRUE) && currentTime <= future_timestamp)
      {
        System.out.println("Eventually within: Verdict is true.");
        m_verdict.setValue(Verdict.Value.TRUE);
        m_verdict.setWitnessTrue(st_v.getWitnessTrue());
        break;
      }
      if (currentTime > future_timestamp && !st_v.is(Verdict.Value.TRUE))
      {
        System.out.println("Eventually within: Verdict is false.");
        m_verdict.setValue(Verdict.Value.FALSE);
        m_verdict.setWitnessFalse(st_v.getWitnessFalse());
        break;
      } 
      if (currentTime > future_timestamp && !prev_st_v.is(Verdict.Value.TRUE) && st_v.is(Verdict.Value.TRUE))
      {
        m_verdict.setValue(Verdict.Value.FALSE);
        m_verdict.setWitnessFalse(st_v.getWitnessFalse());
        break;
      }
      //keep the verdict from last snapshot to
      //evaluate if it was true before this current snapshot
      Verdict prev_st_v = st_v;
    }
    
    return m_verdict;
  }

  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("Eventually within " + m_timestamp + " seconds (\n");
    out.append(m_innerStatement.toString(indent + "  "));
    out.append("\n").append(indent).append(")");
    return out.toString();
  }

  @Override
  public Statement getClone()
  {
    Eventually out = new Eventually();
    out.setInnerStatement(m_innerStatement.getClone());
    return out;
  }

}

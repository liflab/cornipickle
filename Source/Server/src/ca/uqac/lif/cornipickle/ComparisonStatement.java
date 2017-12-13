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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.cornipickle.transformations.CorniTransformation;
import ca.uqac.lif.cornipickle.transformations.PropertyTransformationFactory;
import ca.uqac.lif.cornipickle.transformations.RegexTransformation;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import nl.flotsam.xeger.Xeger;



public abstract class ComparisonStatement extends Statement implements HasTransformations
{
	protected Property m_left;
	protected Property m_right;
	
	protected static Set<CorniTransformation> m_transformations = 
	    new HashSet<CorniTransformation>();
	
	//Identifies if this statement is relative to the past.
	protected boolean m_present;

	ComparisonStatement()
	{
		super();
		m_present = true;
	}

	public final Verdict evaluateTemporal(JsonElement j, Map<String,JsonElement> d)
	{
		JsonElement e1 = m_left.evaluate(j, d);
		JsonElement e2 = m_right.evaluate(j, d);
		
		m_verdict = compare(e1, e2);
		
		if(m_present)
		{
		  generateTransformations(e1, e2, j, d);
		}

		m_present  = false;
		
		return m_verdict;
	}

	@Override
	public final Verdict evaluateAtemporal(JsonElement j, Map<String,JsonElement> d)
	{
		JsonElement e1 = m_left.evaluate(j, d);
		JsonElement e2 = m_right.evaluate(j, d);
		
		Verdict v = compare(e1, e2);
		
		generateTransformations(e1, e2, j, d);
		
		return v;
	}

	public final boolean isTemporal()
	{
		return false;
	}

	public void resetHistory()
	{
		m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
	}

	public void setLeft(final Property p)
	{
		m_left = p;
	}

	public void setRight(final Property p)
	{
		m_right = p;
	}

	protected Verdict compare(JsonElement e1, JsonElement e2)
	{
		if (e1 instanceof JsonString && e2 instanceof JsonString)
		{
			return compare((JsonString) e1, (JsonString) e2);
		}
		else if (e1 instanceof JsonNumber && e2 instanceof JsonNumber)
		{
			return compare((JsonNumber) e1, (JsonNumber) e2);
		}
		return new Verdict(Verdict.Value.FALSE);
	}
	
	protected void generateTransformations(JsonElement e1, JsonElement e2, JsonElement j, Map<String,JsonElement> d)
	{
	  if(!(this instanceof RegexMatch))
	  {
	    if(e1 instanceof JsonNumber && e2 instanceof JsonNumber)
	    {
	      if(m_left instanceof ElementProperty)
	      {
	        ElementProperty left = (ElementProperty) m_left;
	        JsonMap element = null;
	        if(d.get(left.getElementName()) instanceof JsonMap)
	        {
	          element = (JsonMap) d.get(left.getElementName());
	          
	          int id;
	          if(element.containsKey("cornipickleid"))
	          {
	            id = element.getInt("cornipickleid");
	          }
	          else
	          {
	            id = -1;
	          }
	          
	          //Make 3 transformations with values being exactly the right side of the comparison,
	          //the same value + 1, and the same value - 1 to cover cases for a greater than, less than
	          //and equals.
	          JsonNumber e3 = new JsonNumber(((JsonNumber)e2).numberValue().intValue() + 1);
	          JsonNumber e4 = new JsonNumber(((JsonNumber)e2).numberValue().intValue() - 1);
	          m_transformations.add(PropertyTransformationFactory.getInstance(id, left.getPropertyName(), e2));
	          m_transformations.add(PropertyTransformationFactory.getInstance(id, left.getPropertyName(), e3));
	          m_transformations.add(PropertyTransformationFactory.getInstance(id, left.getPropertyName(), e4));
	        }
	      }
	      
	      if(m_right instanceof ElementProperty)
	      {
	        ElementProperty right = (ElementProperty) m_right;
	        JsonMap element = null;
          if(d.get(right.getElementName()) instanceof JsonMap)
          {
            element = (JsonMap) d.get(right.getElementName());
            
            int id;
            if(element.containsKey("cornipickleid"))
            {
              id = element.getInt("cornipickleid");
            }
            else
            {
              id = -1;
            }
            //Make 3 transformations with values being exactly the left side of the comparison,
            //the same value + 1, and the same value - 1 to cover cases for a greater than, less than
            //and equals.
            JsonNumber e3 = new JsonNumber(((JsonNumber)e1).numberValue().intValue() + 1);
            JsonNumber e4 = new JsonNumber(((JsonNumber)e1).numberValue().intValue() - 1);
            m_transformations.add(PropertyTransformationFactory.getInstance(id, right.getPropertyName(), e1));
            m_transformations.add(PropertyTransformationFactory.getInstance(id, right.getPropertyName(), e3));
            m_transformations.add(PropertyTransformationFactory.getInstance(id, right.getPropertyName(), e4));
          }
	      }
	      //If left is an operation and not a constant. If it is a constant, the transformations have been taken care
	      //of above.
	      if (m_left instanceof Operation && !((Operation)m_left).isConstantOperation())
	      {
	        //TODO: See message on slack https://liflab.slack.com/archives/C3QFMFK1P/p1512506387000193
	      }
	      
	      //If right is an operation and not a constant. If it is a constant, the transformations have been taken care
        //of above.
	      if (m_right instanceof Operation && !((Operation)m_right).isConstantOperation())
	      {
	        //TODO: See message on slack https://liflab.slack.com/archives/C3QFMFK1P/p1512506387000193
	      }
	    }
	  }
	  else if(this instanceof RegexMatch)
	  {
	    if(e1 instanceof JsonString && e2 instanceof JsonString)
	    {
	      if(m_left instanceof ElementProperty)
	      {
	        String subject = ((JsonString)e1).stringValue();
	        String pattern = ((JsonString)e2).stringValue();
	        ElementProperty left = (ElementProperty) m_left;
          JsonMap element = (JsonMap) d.get(left.getElementName());
          int id = element.getInt("cornipickleid");
          String otherValue = "";
          
          Verdict v = compare(e1, e2);
          
          if(v.is(Verdict.Value.FALSE))
          {
            Xeger xeger = new Xeger("(?!" + pattern + ")");
            otherValue = xeger.generate();
            m_transformations.add(new RegexTransformation(id, true, left.m_propertyName, pattern, otherValue));
          }
          else
          {
            Xeger xeger = new Xeger(pattern);
            otherValue = xeger.generate();
            m_transformations.add(new RegexTransformation(id, false, left.m_propertyName, pattern, otherValue));
          }
	      }
	    }
	  }
	}
	
	@Override
	public Set<CorniTransformation> getTransformations()
	{
	  return m_transformations;
	}
	
	@Override
	public Set<CorniTransformation> flushTransformations()
	{
	  Set<CorniTransformation> toReturn = new HashSet<CorniTransformation>();
	  toReturn.addAll(m_transformations);
	  m_transformations.clear();
	  return toReturn;
	}

	protected abstract Verdict compare(JsonString e1, JsonString e2);

	protected abstract Verdict compare(JsonNumber e1, JsonNumber e2);

	public abstract String getKeyword();

	@Override
	public final void postfixAccept(LanguageElementVisitor visitor)
	{
		m_left.postfixAccept(visitor);
		m_right.postfixAccept(visitor);
		visitor.visit(this);
		visitor.pop();
	}

	@Override
	public final void prefixAccept(LanguageElementVisitor visitor)
	{
		visitor.visit(this);
		m_left.prefixAccept(visitor);
		m_right.prefixAccept(visitor);
		visitor.pop();
	}
}

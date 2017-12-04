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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cornipickle.transformations.CorniTransformation;
import ca.uqac.lif.cornipickle.transformations.PropertyTransformationFactory;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;

public abstract class ComparisonStatement extends Statement implements HasTransformations
{
	protected Property m_left;
	protected Property m_right;
	
	protected transient List<CorniTransformation> m_transformations;

	ComparisonStatement()
	{
		super();
		m_transformations = new ArrayList<CorniTransformation>();
	}

	public final Verdict evaluateTemporal(JsonElement j, Map<String,JsonElement> d)
	{
		JsonElement e1 = m_left.evaluate(j, d);
		JsonElement e2 = m_right.evaluate(j, d);
		
		generateTransformations(e1, e2, j, d);
		
		m_verdict = compare(e1, e2);
		return m_verdict;
	}

	@Override
	public final Verdict evaluateAtemporal(JsonElement j, Map<String,JsonElement> d)
	{
		JsonElement e1 = m_left.evaluate(j, d);
		JsonElement e2 = m_right.evaluate(j, d);
		return compare(e1, e2);
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
	        JsonMap element = (JsonMap) d.get(left.getElementName());
          int id = element.getInt("cornipickleid");
          JsonNumber e3 = new JsonNumber(((JsonNumber)e2).numberValue().intValue() + 1);
          JsonNumber e4 = new JsonNumber(((JsonNumber)e2).numberValue().intValue() - 1);
          
          m_transformations.add(PropertyTransformationFactory.getInstance(id, left.getPropertyName(), e2));
          m_transformations.add(PropertyTransformationFactory.getInstance(id, left.getPropertyName(), e3));
          m_transformations.add(PropertyTransformationFactory.getInstance(id, left.getPropertyName(), e4));
	      }
	      else if(m_right instanceof ElementProperty)
	      {
	        ElementProperty right = (ElementProperty) m_right;
          JsonMap element = (JsonMap) d.get(right.getElementName());
          int id = element.getInt("cornipickleid");
          JsonNumber e3 = new JsonNumber(((JsonNumber)e1).numberValue().intValue() + 1);
          JsonNumber e4 = new JsonNumber(((JsonNumber)e1).numberValue().intValue() - 1);
          
          m_transformations.add(PropertyTransformationFactory.getInstance(id, right.getPropertyName(), e1));
          m_transformations.add(PropertyTransformationFactory.getInstance(id, right.getPropertyName(), e3));
          m_transformations.add(PropertyTransformationFactory.getInstance(id, right.getPropertyName(), e4));
	      }
	      //If left is an operation and not a constant. If it is a constant, the transformations have been taken care
	      //of above.
	      else if (m_left instanceof Operation && !((Operation)m_left).isConstantOperation())
	      {
	        //TODO
	      }
	    }
	  }
	}
	
	@Override
	public List<CorniTransformation> getTransformations()
	{
	  return m_transformations;
	}
	
	@Override
	public List<CorniTransformation> flushTransformations()
	{
	  List<CorniTransformation> toReturn = new ArrayList<CorniTransformation>();
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

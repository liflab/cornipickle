package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.cornipickle.util.JsonUtils;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;

public class RegexTransformation extends CorniTransformation
{
  protected int m_id;
  protected boolean m_shouldMatch;
  protected String m_property;
  protected String m_regularExpression;
  protected String m_value;
  
  public RegexTransformation(int id, boolean shouldMatch, String property, String reg, String value)
  {
    m_id = id;
    m_shouldMatch = shouldMatch;
    m_property = property;
    m_regularExpression = reg;
    m_value = value;
  }
  
  @Override
  public JsonElement apply(JsonElement in)
  {
    if(!(in instanceof JsonMap))
    {
      return in;
    }
    JsonMap inMap = (JsonMap)in;
    JsonMap out = inMap.clone();
    JsonMap element = JsonUtils.findElementByCornipickleId(out, m_id);
    if(element != null)
    {
      //This is an element like a, p, etc. We need to get its children which should be CDATA
      JsonList children = (JsonList) element.get("children");
      for(JsonElement child : children)
      {
        if(!(child instanceof JsonMap))
        {
          return out;
        }
        JsonMap childMap = (JsonMap) child;
        if(childMap.containsKey("CDATA") && childMap.getInt("cornipickleid") == m_id)
        {
          childMap.put("text", "m_value");
          return out;
        }
      }
    }
    return out;
  }

  @Override
  public boolean conflictsWith(Transformation<JsonElement> t)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int hashCode()
  {
    return 7;
  }

  @Override
  public boolean equals(Object t)
  {
    if(!(t instanceof RegexTransformation))
    {
      return false;
    }
    
    RegexTransformation trans = (RegexTransformation)t;
    if(m_id == trans.m_id && m_shouldMatch == trans.m_shouldMatch &&
        m_regularExpression.equals(trans.m_regularExpression) &&
        m_value.equals(trans.m_value))
    {
      return true;
    }
    return false;
  }

  @Override
  public JsonElement toJson()
  {
    JsonMap map = new JsonMap();
    map.put("type", "regex");
    map.put("id", m_id);
    map.put("shouldmatch", m_shouldMatch);
    map.put("value", m_value);
    map.put("regex", m_regularExpression);
    return map;
  }

}

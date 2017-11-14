package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;

public abstract class ChangePropertyTransformation extends CorniTransformation
{
  protected String m_id;
  protected String m_property;
  protected String m_value;
  
  public ChangePropertyTransformation(String id, String property, String value)
  {
    m_id = id;
    m_property = property;
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
		JsonMap out = new JsonMap();
		out.putAll(inMap);
		JsonMap element = findElementByCornipickleId(out, m_id);
		if(element != null)
		{
		  element.put(m_property, m_value);
		}
		return out;
	}

	@Override
	public boolean conflictsWith(Transformation<JsonElement> t)
	{
		return false;
	}
	
	public JsonMap findElementByCornipickleId(JsonMap in, String id)
	{
	  if(in.containsKey("cornipickleid") && in.get("cornipickleid").equals(id))
	  {
	    return in;
	  }
	  
    if(in.containsKey("children"))
    {
      JsonElement children = in.get("children");
      if(!(children instanceof JsonList))
      {
        assert false;
      
      }
      JsonList childrenlist = (JsonList)children;
      for(JsonElement e : childrenlist)
      {
        if(!(e instanceof JsonMap))
        {
          assert false;
        }
        JsonMap element = (JsonMap)e;
        JsonMap found = findElementByCornipickleId(element, id);
        if(found != null)
        {
          return found;
        }
      }
	  }
    return null;
	}
}

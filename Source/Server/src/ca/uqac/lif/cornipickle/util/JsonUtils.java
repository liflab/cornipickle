package ca.uqac.lif.cornipickle.util;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;

public class JsonUtils
{
  public static JsonMap findElementByCornipickleId(JsonMap in, int id)
  {
    if(in.containsKey("cornipickleid") && in.get("cornipickleid").equals(new JsonNumber(id)))
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

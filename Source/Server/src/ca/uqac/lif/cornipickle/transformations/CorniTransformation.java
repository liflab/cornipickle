package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;

public abstract class CorniTransformation implements Transformation<JsonElement>
{
  @Override
  public abstract int hashCode();
  
  @Override
  public abstract boolean equals(Object t);
  
  public JsonElement toJson()
  {
	  JsonMap map = new JsonMap();
	  map.put("test", "test");
	  return map;
  }
}

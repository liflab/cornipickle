package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;

public abstract class CorniTransformation implements Transformation<JsonElement>
{
  @Override
  public abstract int hashCode();
  
  @Override
  public abstract boolean equals(Object t);
}

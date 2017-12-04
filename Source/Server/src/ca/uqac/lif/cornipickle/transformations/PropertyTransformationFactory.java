package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public final class PropertyTransformationFactory
{
  private PropertyTransformationFactory() {}
  
  public static ChangePropertyTransformation getInstance(int id, String property, JsonElement value)
  {
    if(property.equals("bottom"))
    {
      return new MoveBottomTransformation(id, (JsonNumber)value);
    }
    else if(property.equals("top"))
    {
      return new MoveTopTransformation(id, (JsonNumber)value);
    }
    else if(property.equals("right"))
    {
      return new MoveRightTransformation(id, (JsonNumber)value);
    }
    else if(property.equals("left"))
    {
      return new MoveLeftTransformation(id, (JsonNumber)value);
    }
    else if(property.equals("height"))
    {
      return new ChangeHeightTransformation(id, (JsonNumber)value);
    }
    else if(property.equals("width"))
    {
      return new ChangeWidthTransformation(id, (JsonNumber)value);
    }
    else
    {
      return null;
    }
  }
}

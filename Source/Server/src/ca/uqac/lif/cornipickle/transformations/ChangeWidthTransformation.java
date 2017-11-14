package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;

public class ChangeWidthTransformation extends ChangePropertyTransformation
{
  public ChangeWidthTransformation(String id, String value)
  {
    super(id, "width", value);
  }
  
  @Override
  public boolean conflictsWith(Transformation<JsonElement> t)
  {
    if(t instanceof MoveLeftTransformation || t instanceof MoveRightTransformation)
    {
      return true;
    }
    return false;
  }
  
  @Override
  public int hashCode()
  {
    return 6;
  }
  
  @Override
  public boolean equals(Object t)
  {
    if(!(t instanceof ChangeWidthTransformation))
    {
      return false;
    }
    ChangeWidthTransformation trans = (ChangeWidthTransformation)t;
    if(trans.m_id.equals(m_id) && trans.m_value.equals(m_value))
    {
      return true;
    }
    return false;
  }
}

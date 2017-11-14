package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;

public class MoveLeftTransformation extends ChangePropertyTransformation
{
  public MoveLeftTransformation(String id, String value)
  {
    super(id, "left", value);
  }
  
  @Override
  public boolean conflictsWith(Transformation<JsonElement> t)
  {
    if(t instanceof ChangeWidthTransformation)
    {
      return true;
    }
    return false;
  }
  
  @Override
  public int hashCode()
  {
    return 2;
  }
  
  @Override
  public boolean equals(Object t)
  {
    if(!(t instanceof MoveLeftTransformation))
    {
      return false;
    }
    MoveLeftTransformation trans = (MoveLeftTransformation)t;
    if(trans.m_id.equals(m_id) && trans.m_value.equals(m_value))
    {
      return true;
    }
    return false;
  }
}

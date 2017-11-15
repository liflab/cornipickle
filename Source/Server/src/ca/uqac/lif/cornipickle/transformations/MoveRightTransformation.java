package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class MoveRightTransformation extends ChangePropertyTransformation
{
  public MoveRightTransformation(int id, JsonNumber value)
  {
    super(id, "right", value);
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
    return 3;
  }
  
  @Override
  public boolean equals(Object t)
  {
    if(!(t instanceof MoveRightTransformation))
    {
      return false;
    }
    MoveRightTransformation trans = (MoveRightTransformation)t;
    if(trans.m_id == m_id && trans.m_value.equals(m_value))
    {
      return true;
    }
    return false;
  }
}

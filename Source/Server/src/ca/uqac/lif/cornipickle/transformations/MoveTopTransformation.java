package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;

public class MoveTopTransformation extends ChangePropertyTransformation
{
  public MoveTopTransformation(String id, String value)
  {
    super(id, "top", value);
  }
  
  @Override
  public boolean conflictsWith(Transformation<JsonElement> t)
  {
    if(t instanceof ChangeHeightTransformation)
    {
      return true;
    }
    return false;
  }
  
  @Override
  public int hashCode()
  {
    return 4;
  }
  
  @Override
  public boolean equals(Object t)
  {
    if(!(t instanceof MoveTopTransformation))
    {
      return false;
    }
    MoveTopTransformation trans = (MoveTopTransformation)t;
    if(trans.m_id.equals(m_id) && trans.m_value.equals(m_value))
    {
      return true;
    }
    return false;
  }
}

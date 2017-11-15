package ca.uqac.lif.cornipickle.transformations;

import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class MoveTopTransformation extends ChangePropertyTransformation
{
  public MoveTopTransformation(int id, JsonNumber value)
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
    if(trans.m_id == m_id && trans.m_value.equals(m_value))
    {
      return true;
    }
    return false;
  }
}

package ca.uqac.lif.cornipickle;

public class SetPropertyPossessive extends SetProperty {
  SetPropertyPossessive()
  {
    super();
  }
  
  public SetPropertyPossessive(SetExpression set, String property)
  {
    super(set,property);
  }
  
  public SetPropertyPossessive(SetExpression set, StringConstant property)
  {
    super(set,property);
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append(m_set.toString()).append("'s ").append(m_property);
    return out.toString();
  }

  @Override
  public SetPropertyComplement getClone()
  {
    SetPropertyComplement out = new SetPropertyComplement(m_set, m_property);
    return out;
  }
}

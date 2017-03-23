package ca.uqac.lif.cornipickle;

public class SetPropertyComplement extends SetProperty {
  
  SetPropertyComplement()
  {
    super();
  }
  
  public SetPropertyComplement(SetExpression set, String property)
  {
    super(set,property);
  }
  
  public SetPropertyComplement(SetExpression set, StringConstant property)
  {
    super(set,property);
  }
  
  @Override
  public String toString(String indent)
  {
    StringBuilder out = new StringBuilder();
    out.append("the ").append(m_property).append(" of ").append(m_set.toString());
    return out.toString();
  }

  @Override
  public SetPropertyComplement getClone()
  {
    SetPropertyComplement out = new SetPropertyComplement(m_set, m_property);
    return out;
  }
}

package ca.uqac.lif.cornipickle;

import java.util.List;
import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public abstract class SetProperty extends Property {
  
  protected SetExpression m_set;
  
  protected String m_property;
  
  SetProperty()
  {
    super();
    m_set = null;
    m_property = null;
  }
  
  public SetProperty(SetExpression set, String property)
  {
    super();
    setSet(set);
    setProperty(property);
  }
  
  public SetProperty(SetExpression set, StringConstant property)
  {
    super();
    setSet(set);
    setProperty(property.toString());
  }
  
  public void setSet(SetExpression set)
  {
    m_set = set;
  }
  
  public void setProperty(String property)
  {
    m_property = property;
  }
  
  public SetExpression getSet()
  {
    return m_set;
  }
  
  public String getProperty()
  {
    return m_property;
  }

  @Override
  public JsonElement evaluate(JsonElement t, Map<String, JsonElement> d)
  {
    JsonElement e = null;
    
    List<JsonElement> list = m_set.evaluate(t, d);
    
    if(m_property.equals("size"))
    {
      e = new JsonNumber(list.size());
    }
    return e;
  }
  
  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    m_set.prefixAccept(visitor);
    visitor.pop();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    m_set.postfixAccept(visitor);
    visitor.visit(this);
    visitor.pop();
  }

  @Override
  public String toString(String indent)
  {
    // TODO Auto-generated method stub
    return null;
  }

}

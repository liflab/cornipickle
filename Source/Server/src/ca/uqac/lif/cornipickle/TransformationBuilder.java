package ca.uqac.lif.cornipickle;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.cornipickle.transformations.ChangeHeightTransformation;
import ca.uqac.lif.cornipickle.transformations.ChangeWidthTransformation;
import ca.uqac.lif.cornipickle.transformations.CorniTransformation;
import ca.uqac.lif.cornipickle.transformations.MoveBottomTransformation;
import ca.uqac.lif.cornipickle.transformations.MoveLeftTransformation;
import ca.uqac.lif.cornipickle.transformations.MoveRightTransformation;
import ca.uqac.lif.cornipickle.transformations.MoveTopTransformation;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;

public class TransformationBuilder implements LanguageElementVisitor {
  
  private Set<CorniTransformation> m_transformations;
  
  private JsonElement m_page;
  
  private Set<Integer> m_ids;
  
  public TransformationBuilder(JsonElement page)
  {
    m_transformations = new HashSet<CorniTransformation>();
    m_page = page;
    m_ids = fetchPageIds(m_page);
  }
  
  public Set<Integer> getIds()
  {
    return m_ids;
  }
  
  public Set<CorniTransformation> getTransformations()
  {
    return m_transformations;
  }

  @Override
  public void visit(LanguageElement e)
  {
    if(e instanceof ElementProperty)
    {
      ElementProperty property = (ElementProperty)e;
      if(property.getPropertyName().equals("bottom"))
      {
        for(Integer id : fetchPageIds(m_page))
        {
          int pageHeight = (int) (long) ((JsonMap)m_page).getNumber("height");
          for(int i = 0; i < pageHeight; i++)
          {
            m_transformations.add(new MoveBottomTransformation(id, new JsonNumber(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("top"))
      {
        for(Integer id : fetchPageIds(m_page))
        {
          int pageHeight = (int) (long) ((JsonMap)m_page).getNumber("height");
          for(int i = 0; i < pageHeight; i++)
          {
            m_transformations.add(new MoveTopTransformation(id, new JsonNumber(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("left"))
      {
        for(Integer id : fetchPageIds(m_page))
        {
          int pageWidth = (int) (long) ((JsonMap)m_page).getNumber("width");
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new MoveLeftTransformation(id, new JsonNumber(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("right"))
      {
        for(Integer id : fetchPageIds(m_page))
        {
          int pageWidth = (int) (long) ((JsonMap)m_page).getNumber("width");
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new MoveRightTransformation(id, new JsonNumber(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("width"))
      {
        for(Integer id : fetchPageIds(m_page))
        {
          int pageWidth = (int) (long) ((JsonMap)m_page).getNumber("width");
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new ChangeWidthTransformation(id, new JsonNumber(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("height"))
      {
        for(Integer id : fetchPageIds(m_page))
        {
          int pageHeight = (int) (long) ((JsonMap)m_page).getNumber("height");
          for(int i = 0; i < pageHeight; i++)
          {
            m_transformations.add(new ChangeHeightTransformation(id, new JsonNumber(i)));
          }
        }
      }
    }
    else if(e instanceof PredicateCall)
    {
    	PredicateCall pc = (PredicateCall) e;
    	pc.getPredicateDefinition().postfixAccept(this);
    }
  }
  
  private Set<Integer> fetchPageIds(JsonElement page)
  {
    Set<Integer> ids = new HashSet<Integer>();
    
    if(!(page instanceof JsonMap))
    {
      assert false;
      return ids;
    }
    
    JsonMap element = (JsonMap)page;
    if(element.containsKey("cornipickleid"))
    {
      ids.add((int) (long) element.getNumber("cornipickleid"));
    }
    if(element.containsKey("children"))
    {
      JsonElement children = element.get("children");
      if(!(children instanceof JsonList))
      {
        assert false;
      }
      JsonList childrenlist = (JsonList)children;
      for(JsonElement e : childrenlist)
      {
        ids.addAll(fetchPageIds(e));
      }
    }
    return ids;
  }

  @Override
  public void pop() {
    // TODO Auto-generated method stub
    
  }

}

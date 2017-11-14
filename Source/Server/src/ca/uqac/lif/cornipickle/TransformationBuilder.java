package ca.uqac.lif.cornipickle;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.cornipickle.faultfinder.Expression;
import ca.uqac.lif.cornipickle.faultfinder.Transformation;
import ca.uqac.lif.cornipickle.faultfinder.logic.CorniExpressionVisitor;
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

public class TransformationBuilder implements CorniExpressionVisitor {
  
  private Set<CorniTransformation> m_transformations;
  
  private JsonElement m_page;
  
  private Set<String> m_ids;
  
  public TransformationBuilder(JsonElement page)
  {
    m_transformations = new HashSet<CorniTransformation>();
    m_page = page;
    m_ids = fetchPageIds(m_page);
  }

  @Override
  public void visit(Expression<JsonElement> expression) {
  }

  @Override
  public void visit(Property e) {
    if(e instanceof ElementProperty)
    {
      ElementProperty property = (ElementProperty)e;
      if(property.getPropertyName().equals("bottom"))
      {
        for(String id : fetchPageIds(m_page))
        {
          int pageWidth = Integer.parseInt(((JsonMap)m_page).getString("height"));
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new MoveBottomTransformation(id, String.valueOf(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("top"))
      {
        for(String id : fetchPageIds(m_page))
        {
          int pageWidth = Integer.parseInt(((JsonMap)m_page).getString("height"));
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new MoveTopTransformation(id, String.valueOf(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("left"))
      {
        for(String id : fetchPageIds(m_page))
        {
          int pageWidth = Integer.parseInt(((JsonMap)m_page).getString("width"));
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new MoveLeftTransformation(id, String.valueOf(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("right"))
      {
        for(String id : fetchPageIds(m_page))
        {
          int pageWidth = Integer.parseInt(((JsonMap)m_page).getString("width"));
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new MoveRightTransformation(id, String.valueOf(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("width"))
      {
        for(String id : fetchPageIds(m_page))
        {
          int pageWidth = Integer.parseInt(((JsonMap)m_page).getString("width"));
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new ChangeWidthTransformation(id, String.valueOf(i)));
          }
        }
      }
      else if(property.getPropertyName().equals("height"))
      {
        for(String id : fetchPageIds(m_page))
        {
          int pageWidth = Integer.parseInt(((JsonMap)m_page).getString("height"));
          for(int i = 0; i < pageWidth; i++)
          {
            m_transformations.add(new ChangeHeightTransformation(id, String.valueOf(i)));
          }
        }
      }
    }
  }
  
  private Set<String> fetchPageIds(JsonElement page)
  {
    Set<String> ids = new HashSet<String>();
    
    if(!(page instanceof JsonMap))
    {
      assert false;
      return ids;
    }
    
    JsonMap element = (JsonMap)page;
    if(element.containsKey("cornipickleid"))
    {
      ids.add(element.getString("cornipickleid"));
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

}

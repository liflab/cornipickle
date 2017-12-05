package ca.uqac.lif.cornipickle;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ca.uqac.lif.cornipickle.transformations.CorniTransformation;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;

public class ComparisonStatementTest
{
  @Test
  public void flushTransformationsTest()
  {
    ComparisonStatement s = new EqualsStatement();
    
    s.setLeft(new ElementPropertyPossessive("$x", "bottom"));
    s.setRight(new NumberConstant(4));
    
    JsonMap element = new JsonMap();
    element.put("cornipickleid", new JsonNumber(1));
    element.put("bottom", new JsonNumber(5));
    
    JsonMap d = new JsonMap();
    d.put("$x", element);
    
    s.evaluateTemporal(new JsonMap(), d);
    
    assertTrue(s.getTransformations().size() > 0);
    
    List<CorniTransformation> trans = s.flushTransformations();
    
    assertTrue(trans.size() > 0);
    assertTrue(s.getTransformations().size() == 0);
  }
}
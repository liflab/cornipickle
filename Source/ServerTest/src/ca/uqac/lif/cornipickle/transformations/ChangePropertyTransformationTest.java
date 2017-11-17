package ca.uqac.lif.cornipickle.transformations;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.util.JsonUtils;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class ChangePropertyTransformationTest
{
  protected JsonParser s_parser = new JsonParser();
  protected Interpreter m_interpreter;
  
  @Before
  public void setUp() {
      m_interpreter = new Interpreter();
  }
  
  @Test
  public void applyTest()
  {
    try {
      JsonElement page = s_parser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json"));
      
      ChangePropertyTransformation trans = new MoveBottomTransformation(3, new JsonNumber(6));
      JsonElement modifiedPage = trans.apply(page);
      if(JsonUtils.findElementByCornipickleId((JsonMap)modifiedPage, 3).getNumber("bottom").intValue() == 6)
      {
        assert true;
      }
      else
      {
        assert false;
      }
    } catch (JsonParseException e) {
      assert false;
    }
  }
  
  @Test
  public void hashcodeAndEqualsTest()
  {
    Set<CorniTransformation> theset = new HashSet<CorniTransformation>();
    MoveBottomTransformation trans1 = new MoveBottomTransformation(1, new JsonNumber(1));
    MoveBottomTransformation trans2 = new MoveBottomTransformation(1, new JsonNumber(1));
    MoveTopTransformation trans3 = new MoveTopTransformation(1, new JsonNumber(1));
    MoveTopTransformation trans4 = new MoveTopTransformation(1, new JsonNumber(2));
    
    //Check if two of same class same parameters create a collision
    theset.add(trans1);
    theset.add(trans2);
    if(theset.size() != 1)
    {
      assert false;
    }
    
    //Check if adding a different class but same parameters doesn't create a collision
    theset.add(trans3);
    if(theset.size() != 2)
    {
      assert false;
    }
    
    //Check if adding same class different parameters doesn't create a collision
    theset.add(trans4);
    if(theset.size() != 3)
    {
      assert false;
    }
  }
}

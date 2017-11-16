package ca.uqac.lif.cornipickle.transformations;

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
}

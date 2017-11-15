package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class TransformationBuilderTest {
  
  protected JsonParser s_parser = new JsonParser();
  protected Interpreter m_interpreter;
  
  @Before
  public void setUp() {
      m_interpreter = new Interpreter();
  }

  @Test
  public void fetchPageIdsTest()
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json");
    try {
      JsonElement page = s_parser.parse(json);
      //m_interpreter.parseProperties("There exists $x in $(div>a) such that ( $x's bottom is 12 ).");
      TransformationBuilder builder = new TransformationBuilder(page);
      
      Set<Integer> ids = builder.getIds();
      if(ids.contains(1) && ids.contains(2) && ids.contains(3) && ids.contains(4))
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

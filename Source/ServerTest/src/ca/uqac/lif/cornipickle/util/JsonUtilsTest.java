package ca.uqac.lif.cornipickle.util;

import org.junit.Test;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class JsonUtilsTest
{
  protected JsonParser s_parser = new JsonParser();
  
  @Test
  public void findElementByCornipickleIdTest()
  {
    try {
      JsonElement page = s_parser.parse(PackageFileReader.readPackageFile(this.getClass(), "../data/sample-12.json"));
      
      JsonElement found = JsonUtils.findElementByCornipickleId((JsonMap)page, 2);
      
      if(((JsonMap)found).getNumber("cornipickleid").intValue() == 2)
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

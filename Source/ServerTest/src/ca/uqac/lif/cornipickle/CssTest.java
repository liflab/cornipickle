package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonFastParser;
import ca.uqac.lif.cornipickle.json.JsonMap;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonSlowParser;
import ca.uqac.lif.cornipickle.json.JsonString;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.util.FileReadWrite;

@SuppressWarnings("unused")
public class CssTest
{

  CornipickleParser parser;
  
  JsonParser m_jsonParser;
  
  @Before
  public void setUp() throws Exception
  {
    parser = new CornipickleParser();
    m_jsonParser = new JsonFastParser();
  }

  @Test
  public void testCssFetch1() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("body h1", (JsonMap) jse);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetch2() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-4.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("h1 p", (JsonMap) jse);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 2)
    {
      fail("Expected size 2, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetch3() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-4.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch(".maclasse", (JsonMap) jse);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 2)
    {
      fail("Expected size 2, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetch4() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-9.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch(".square", (JsonMap) jse);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 3)
    {
      fail("Expected size 2, got " + list.size());
    }
  }

}

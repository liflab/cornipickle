package ca.uqac.lif.cornipickle.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.NodePath;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.util.FileReadWrite;

@SuppressWarnings("unused")
public class JsonTest
{
  BnfParser parser;
  
  @Before
  public void setUp() throws Exception
  {
    parser = new BnfParser();
    String grammar = PackageFileReader.readPackageFile(JsonParser.getGrammarStream());
    parser.setGrammar(grammar);
  }

  @Test
  public void testSimpleObject()
  {
    String json = "{ \"a\" : 0 }";
    ParseNode pn = shouldParseAndNotNull(json, "<S>");
    int expected_size = 13;
    int obtained_size = pn.getSize();
    if (expected_size != obtained_size)
    {
      fail("Expected tree of size " + expected_size + ", got " + obtained_size);
    }
  }
  
  @Test
  public void testSimpleList()
  {
    String json = "[0, 1, 2, \"abc\" ]";
    ParseNode pn = shouldParseAndNotNull(json, "<S>");
    int expected_size = 23;
    int obtained_size = pn.getSize();
    if (expected_size != obtained_size)
    {
      fail("Expected tree of size " + expected_size + ", got " + obtained_size);
    }
  }
  
  @Test
  public void testString()
  {
    String json = "\"abcdef\"";
    ParseNode pn = shouldParseAndNotNull(json, "<S>");
    int expected_size = 3;
    int obtained_size = pn.getSize();
    if (expected_size != obtained_size)
    {
      fail("Expected tree of size " + expected_size + ", got " + obtained_size);
    }
  }
  
  @Test
  public void testNumber()
  {
    String json = "12345";
    ParseNode pn = shouldParseAndNotNull(json, "<S>");
    int expected_size = 3;
    int obtained_size = pn.getSize();
    if (expected_size != obtained_size)
    {
      fail("Expected tree of size " + expected_size + ", got " + obtained_size);
    }
  }
  
  @Test
  public void testSample() throws IOException
  {
    String json = FileReadWrite.readFile("data/sample.json");
    ParseNode pn = shouldParseAndNotNull(json, "<S>");
  }
  
  @Test
  public void testParser1() throws IOException, JsonParseException
  {
    String json = FileReadWrite.readFile("data/sample.json");
    JsonElement jse = JsonParser.parse(json);
    if (jse == null)
    {
      fail("Element is null");
    }
  }
  
  @Test
  public void testParser4() throws IOException, JsonParseException
  {
    String json = FileReadWrite.readFile("data/sample-5.json");
    JsonElement jse = JsonParser.parse(json);
    if (jse == null)
    {
      fail("Element is null");
    }
  }
  
  @Test
  public void testParser2() throws IOException, JsonParseException
  {
    String json = FileReadWrite.readFile("data/sample-2.json");
    JsonElement jse = JsonParser.parse(json);
    if (jse == null)
    {
      fail("Element is null");
    }
  }
  
  @Test
  public void testParser3() throws IOException, JsonParseException
  {
    JsonElement jse = JsonParser.parse("[ ]");
    if (jse == null)
    {
      fail("Element is null");
    }
  }
  
  @Test
  public void testGet1() throws IOException, JsonParseException
  {
    String json = FileReadWrite.readFile("data/sample.json");
    JsonElement jse = JsonParser.parse(json);
    JsonElement answer = JsonPath.get(jse, "children[0].tagname");
    if (!(answer instanceof JsonString))
    {
      fail("Expected string, got something else");
    }
    JsonString s_answer = (JsonString) answer;
    if (s_answer.stringValue().compareTo("h1") != 0)
    {
      fail("Expected h1, got " + s_answer.stringValue());
    }
  }
  
  @Test
  public void testGet2() throws IOException, JsonParseException
  {
    String json = FileReadWrite.readFile("data/sample-4.json");
    JsonElement jse = JsonParser.parse(json);
    JsonElement answer = JsonPath.get(jse, "children[0].children[1].children[0].children[0].tagname");
    if (!(answer instanceof JsonString))
    {
      fail("Expected string, got something else");
    }
    JsonString s_answer = (JsonString) answer;
    if (s_answer.stringValue().compareTo("#CDATA") != 0)
    {
      fail("Expected #CDATA, got " + s_answer.stringValue());
    }
  }
  
  public ParseNode shouldParseAndNotNull(String line, String start_symbol)
  {
    parser.setStartRule(start_symbol);
    ParseNode pn = null;
    try
    {
      pn = parser.parse(line);
    } catch (ParseException e)
    {
      fail(e.toString());
    }
    if (pn == null)
    {
      fail("Failed parsing: returned null");
    }
    return pn;
  }

}

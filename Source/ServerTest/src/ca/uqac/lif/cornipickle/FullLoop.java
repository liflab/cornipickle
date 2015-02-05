package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonSlowParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.util.FileReadWrite;

public class FullLoop
{
  CornipickleParser c_parser;
  JsonParser j_parser;
  
  @Before
  public void setUp() throws Exception
  {
    c_parser = new CornipickleParser();
    j_parser = new JsonSlowParser();
  }

  @Test
  public void testForEach1() throws ParseException, JsonParseException, IOException
  {
    String property = "For each $x in $(p.maclasse) ($x's width equals 100)";
    Statement st = c_parser.parseStatement(property);
    if (st == null)
    {
      fail("Parsed statement is null");
    }
    JsonElement document = j_parser.parse(FileReadWrite.readFile("data/sample-4.json"));
    if (document == null)
    {
      fail("JSON document is null");
    }
    Verdict verdict = st.evaluate(document);
    if (!verdict.is(Verdict.Value.TRUE))
    {
      fail("Wrong verdict");
    }
  }
  
  @Test
  public void testForEach2() throws ParseException, JsonParseException, IOException
  {
    String property = "For each $x in $(p) ($x's width equals 100)";
    Statement st = c_parser.parseStatement(property);
    if (st == null)
    {
      fail("Parsed statement is null");
    }
    JsonElement document = j_parser.parse(FileReadWrite.readFile("data/sample-4.json"));
    if (document == null)
    {
      fail("JSON document is null");
    }
    Verdict verdict = st.evaluate(document);
    if (!verdict.is(Verdict.Value.FALSE))
    {
      fail("Wrong verdict");
    }
  }

}

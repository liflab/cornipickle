package ca.uqac.lif.cornipickle;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.util.FileReadWrite;

@SuppressWarnings("unused")
public class GrammarTest
{
  CornipickleParser parser;
  
  @Before
  public void setUp() throws Exception
  {
    parser = new CornipickleParser();
  }
  
  @Test
  public void testUserDefinedSet2() throws ParseException
  {
    String line = "A tomato is any of \"abc\", \"def\", \"h1f\"";
    ParseNode pn = shouldParseAndNotNull(line, "<def_set>", false);
  }
  
  @Test
  public void testUserDefinedSet1() throws ParseException
  {
    String line = "A tomato is any of \"abc\"";
    ParseNode pn = shouldParseAndNotNull(line, "<def_set>", true);
  }
  
  @Test
  public void testEquality1() throws ParseException
  {
    String line = "\"3\" equals \"3\"";
    ParseNode pn = shouldParseAndNotNull(line, "<equality>", false);
  }
  
  @Test
  public void testEquality2() throws ParseException
  {
    String line = "$x's height equals \"3\"";
    ParseNode pn = shouldParseAndNotNull(line, "<equality>", false);
  }
  
  @Test
  public void testForEach1() throws ParseException
  {
    String line = "For each $x in $(p.menu) (\"3\" equals \"3\")";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>", false);
  }
  
  @Test
  public void testForEach2() throws ParseException
  {
    String line = "For each $x in $(p) ($x's width equals 100)";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>", false);
  }

  @Test
  public void testCssSelector1() throws ParseException
  {
    String line = "$(p1.menu)";
    ParseNode pn = shouldParseAndNotNull(line, "<css_selector>", false);
  }
  
  @Test
  public void testCssSelector2() throws ParseException
  {
    String line = "$(p1.menu h1 h2#myid)";
    ParseNode pn = shouldParseAndNotNull(line, "<css_selector>", false);
  }
  
  @Test
  public void testPredicate1() throws ParseException
  {
    String line = "We say that all is good when (1 equals 1)";
    ParseNode pn = shouldParseAndNotNull(line, "<predicate>", false);
  }
  
  public ParseNode shouldParseAndNotNull(String line, String start_symbol, boolean debug_mode)
  {
    BnfParser p = parser.getParser();
    p.setStartRule(start_symbol);
    p.setDebugMode(debug_mode);
    ParseNode pn = null;
    try
    {
      pn = p.parse(line);
    } catch (ParseException e)
    {
      fail(e.toString());
    }
    if (pn == null)
    {
      fail("Failed parsing through grammar: returned null");
    }
    return pn;
  }

}

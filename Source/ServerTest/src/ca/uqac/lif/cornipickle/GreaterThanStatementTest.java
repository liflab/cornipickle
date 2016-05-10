package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.json.JsonString;

public class GreaterThanStatementTest {
	
	CornipickleParser parser;
	GreaterThanStatement gts;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();
		String line = "$y's right is greater than $x's left";

	    ParseNode pn = shouldParseAndNotNull(line, "<gt>");
	    LanguageElement e = parser.parseStatement(pn);

	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof GreaterThanStatement))
	    {
	      fail("Got wrong type of object");
	    }
	    gts=(GreaterThanStatement)e;

	}
	@Test
	public void testgetkeyWord() {				
		assertTrue(gts.getKeyword().equals("is greater than"));
	}
	@Test
	public void testcompareString1(){
		JsonString js1 =new JsonString("A");
		JsonString js2 =new JsonString("A");
		assertTrue(gts.compare(js1, js2).getValue().equals(Verdict.Value.FALSE));
	}
	@Test
	public void testcompareString2(){
		JsonString js1 =new JsonString("B");
		JsonString js2 =new JsonString("A");
		assertTrue(gts.compare(js1, js2).getValue().equals(Verdict.Value.TRUE));
	}
	@Test
	public void testcompareString3(){
		JsonString js1 =new JsonString("3");
		JsonString js2 =new JsonString("3");
		assertTrue(gts.compare(js1, js2).getValue().equals(Verdict.Value.FALSE));
	}
	@Test
	public void testcompareString4(){
		JsonString js1 =new JsonString("A");
		JsonString js2 =new JsonString("3");
		assertTrue(gts.compare(js1, js2).getValue().equals(Verdict.Value.TRUE));
	}
	@Test
	public void testcompareString5(){
		JsonString js1 =new JsonString("3");
		JsonString js2 =new JsonString("A");
		assertTrue(gts.compare(js1, js2).getValue().equals(Verdict.Value.FALSE));
	}
	  public ParseNode shouldParseAndNotNull(String line, String start_symbol){
		    BnfParser p = parser.getParser();
		    //p.setDebugMode(true);
		    p.setStartRule(start_symbol);
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
		      fail("Failed parsing expression through grammar: returned null");
		    }
		    return pn;
		  }

}

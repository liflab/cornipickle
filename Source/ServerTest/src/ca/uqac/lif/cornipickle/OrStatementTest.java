package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class OrStatementTest {
	CornipickleParser parser;
	OrStatement or;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();

	    String line = "($y's right is greater than $x's left) Or\n($x's right is greater than $y's left)";

	    ParseNode pn = shouldParseAndNotNull(line, "<or>");
	    LanguageElement e = parser.parseStatement(pn);

	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof OrStatement))
	    {
	      fail("Got wrong type of object");
	    }
	    or=(OrStatement)e;

	}
	@Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);
		JsonElement je2= new JsonNumber(7);		
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		test.put("5", je2);			
		assertTrue(or.evaluateAtemporal(je, test).toString().equals("F {\n\n}"));
	}

	@Test
	public void testEvaluatetemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);
		JsonElement je2= new JsonNumber(7);		
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		test.put("5", je2);			
		assertTrue(or.evaluateTemporal(je, test).toString().equals("F {\n\n}"));
	}

	@Test
	public void testGetClone() {
		OrStatement or2;
		or2= or.getClone();
		assertTrue(or2.toString().equals(or.toString()));
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

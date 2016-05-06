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

public class NeverTest {
	CornipickleParser parser;
	Never nv;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();
		String line = "Never (\"3\" is less than \"3\")\n";

	    ParseNode pn = shouldParseAndNotNull(line, "<never>");
	    LanguageElement e = parser.parseStatement(pn);

	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof Never))
	    {
	      fail("Got wrong type of object");
	    }
	    nv=(Never)e;
	}

	@Test
	public void testToStringString() {
		assertTrue(nv.toString().equals("Never (\n3 is less than 3\n)"));
	}

	@Test
	public void testEvaluateTemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);
		JsonElement je2= new JsonNumber(7);		
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		test.put("5", je2);	
		
		assertTrue(nv.evaluateTemporal(je, test).toString().equals("?"));		
	}

	@Test
	public void testGetClone() {
		Never nv2;
		nv2=(Never)nv.getClone();
		assertTrue(nv2.toString().equals(nv.toString()));
	}

	@Test
	public void testNever() {
		Never nv2=new Never();
		assertTrue(nv2 instanceof Never);
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

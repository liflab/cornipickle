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

public class EventuallyWithinTest {
	CornipickleParser parser;
	EventuallyWithin ew;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();

		String line = "Eventually within 3 seconds ( \"3\" equals \"3\")\n";

	    ParseNode pn = shouldParseAndNotNull(line, "<eventually_within>");
	    LanguageElement e = parser.parseStatement(pn);

	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof EventuallyWithin))
	    {
	      fail("Got wrong type of object");
	    }
	    ew=(EventuallyWithin)e;
	}
	@Test
	public void testToStringString() {
		assertTrue(ew.toString().equals("Eventually within 3.0 seconds (\n3 equals 3\n)"));
	}

	@Test
	public void testEvaluateTemporal() {//à completer
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		assertTrue(ew.evaluate(je, test).m_value.equals(Verdict.Value.TRUE));		
	}
	@Test
	public void testGetClone() {
		EventuallyWithin ew2 =new EventuallyWithin();
		ew2=(EventuallyWithin) ew.getClone();
		assertTrue(ew2.toString().equals(ew.toString()));
		
	}/*

	@Test
	public void testEventuallyWithin() {
		fail("Not yet implemented");
	}

	@Test
	public void testEventuallyWithinNumberConstant() {
		fail("Not yet implemented");
	}*/
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

package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

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

	/*@Test
	public void testEvaluateTemporal() {
		fail("Not yet implemented");
	}

	*/@Test
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

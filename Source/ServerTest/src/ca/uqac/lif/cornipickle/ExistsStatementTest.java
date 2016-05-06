package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

public class ExistsStatementTest {
	CornipickleParser parser;
	ExistsStatement es;
	@Before
	public void setUp() throws Exception {
		parser= new CornipickleParser();
		String line = "There exists $d in $(#d) such that ( $d's width equals (200 + 100) )";
	    ParseNode pn = shouldParseAndNotNull(line, "<exists>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof ExistsStatement))
	    {
	      fail("Got wrong type of object");
	    }
	    es=(ExistsStatement)e;
	}
	@Test
	public void testToStringString() {
		assertTrue(es.toString().equals("There exists a $d in $(#d) such that\n$d's width equals 200.0 + 100.0"));
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

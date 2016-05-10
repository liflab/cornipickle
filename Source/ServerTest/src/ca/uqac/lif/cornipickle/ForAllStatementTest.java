package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

public class ForAllStatementTest {
	
	CornipickleParser parser;
	ForAllStatement fas;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();
	    String line = "For each $x in $(p.menu) (\"3\" equals \"3\")";
	    ParseNode pn = shouldParseAndNotNull(line, "<foreach>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof ForAllStatement))
	    {
	      fail("Got wrong type of object");
	    }
	    fas=(ForAllStatement)e;
	}
	@Test
	public void testToStringString() {
		assertTrue(fas.toString().equals("For each $x in $(p.menu)\n3 equals 3"));
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

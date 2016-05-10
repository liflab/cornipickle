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

public class AndStatementTest {
	CornipickleParser parser;
	AndStatement as;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();
		String line = "($y's right is greater than $x's left)\n" +
	            "  And\n" +
	            "  ($x's right is greater than $y's left)";

	    ParseNode pn = shouldParseAndNotNull(line, "<and>");
	    LanguageElement e = parser.parseStatement(pn);

	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof AndStatement))
	    {
	      fail("Got wrong type of object");
	    }
	    as=(AndStatement)e;

	}

	@Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je=new JsonNumber(3);
		Map<String,JsonElement>map=new HashMap<String,JsonElement>();
		assertTrue(as.evaluateAtemporal(je, map).getValue().equals(Verdict.Value.FALSE));
		//a reverifier
	}

	@Test
	public void testGetClone() {
		AndStatement as2=as.getClone();
		assertTrue(as2.toString().equals(as.toString()));
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

package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class StatementTest {
	
	 CornipickleParser parser;
	 NegationStatement ns;
	@Before
    public void setUp() throws Exception
    {
        parser = new CornipickleParser();

        String line = "Not (\"2\" is less than \"4\")\n";

        ParseNode pn = shouldParseAndNotNull(line, "<negation>");
        LanguageElement e = parser.parseStatement(pn);

        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof NegationStatement))
        {
            fail("Got wrong type of object");
        }

        ns = (NegationStatement)e;

    }
	
	@Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je =new JsonNumber(3);
		assertTrue(ns.evaluateAtemporal(je).getValue().equals(Verdict.Value.FALSE));
	}
    public ParseNode shouldParseAndNotNull(String line, String start_symbol)
    {
        BnfParser p = parser.getParser();
        //p.setDebugMode(true);
        p.setStartRule(start_symbol);
        ParseNode pn = null;

        try
        {
            pn = p.parse(line);
        } catch (BnfParser.ParseException e)
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

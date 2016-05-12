package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by paul on 03/05/16.
 */
public class NegationStatementTest {

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
    public void TestNegationStatementGetStatement(){
        assertTrue("2 is less than 4".equals(ns.getStatement().toString()));
    }

    @Test
    public void TestNegationStatementResetHistory(){
        ns.resetHistory();
        assertTrue(ns.m_verdict.is(Verdict.Value.INCONCLUSIVE));
    }


    @Test
    public void TestNegationStatementToString(){
        String expected = ns.toString();
        assertTrue(expected.equals("Not (2 is less than 4)"));
    }

    @Test
    public void TestNegationStatementGetClone(){
        NegationStatement ns2 = ns.getClone();
        assertTrue(ns2.m_innerStatement.equals(ns.m_innerStatement));
    }

    @Test
    public void TestNegationStatementIsTemporal(){
        boolean expected = ns.m_innerStatement.isTemporal();
        assertTrue(expected==ns.isTemporal());
    }
    @Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);
		JsonElement je2= new JsonNumber(7);		
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		test.put("Trololo", je2);	
		assertTrue(ns.evaluateAtemporal(je,test).toPlainString().equals("FALSE"));		
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

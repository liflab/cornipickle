package ca.uqac.lif.cornipickle;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paul on 03/05/16.
 */
public class LetStatementTest {

    CornipickleParser parser;
    LetStatement ls;

    @Before
    public void setUp() throws Exception
    {
        parser = new CornipickleParser();

        String line = "Let $arf be $x's height (\"3\" equals \"3\")";
        ParseNode pn = shouldParseAndNotNull(line, "<let>");
        LanguageElement e = parser.parseStatement(pn);
        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof LetStatement))
        {
            fail("Got wrong type of object");
        }

        ls = (LetStatement)e;

    }


    @Test
    public void TestLetStatemenToString(){
        String expected = "let $arf be $x's height (\n3 equals 3\n)";
        assertTrue(expected.equals(ls.toString()));
    }

    @Test
    public void TestLetStatementIsTemporal(){
        assertFalse(ls.isTemporal());
    }

    @Test
    public void TestNegationStatementResetHistory(){
        ls.resetHistory();
        assertTrue(ls.m_verdict.is(Verdict.Value.INCONCLUSIVE));
    }

    @Test
    public void TestLetStatementGetVariable(){
        String expected = "$arf";
        assertTrue(expected.equals(ls.getVariable()));
    }

    @Test
    public void TestLetStatementDefaultConstructor(){
        LetStatement ls = new LetStatement();
        assertTrue(ls.getVariable().equals("")&&ls.m_property==null&&ls.m_value==null);
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

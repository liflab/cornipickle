package ca.uqac.lif.cornipickle;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by paul on 03/05/16.
 */
public class LessThanStatementTest {

    CornipickleParser parser;
    LessThanStatement ls;

    @Before
    public void setUp(){
        parser = new CornipickleParser();

        String line = "\"3\" is less than \"4\"\n";

        ParseNode pn = shouldParseAndNotNull(line, "<lt>");
        LanguageElement e = parser.parseStatement(pn);

        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof LessThanStatement))
        {
            fail("Got wrong type of object");
        }

        ls = (LessThanStatement) e;
    }




    @Test
    public void TestLessThanStatementToString(){
        String expected = "3 is less than 4";
        assertTrue(expected.equals(ls.toString()));
    }

    @Test
    public void TestLessThanStatementGetClone(){
        LessThanStatement lsClone = ls.getClone();
        assertTrue(lsClone.m_left.toString().equals(ls.m_left.toString())&&lsClone.m_right.toString().equals(ls.m_right.toString()));
    }

    @Test
    public void TestLessThanStatementGetKeyword(){
        String expected = "is less than";
        assertTrue(expected.equals(ls.getKeyword()));
    }

    @Test
    public void TestLessThanStatementCompareNumberJSonTRUE(){
        JsonNumber n1 = new JsonNumber(3);
        JsonNumber n2 = new JsonNumber(4);

        Verdict expected = new Verdict(Verdict.Value.TRUE);

        Verdict result = ls.compare(n1,n2);

        assertTrue(expected.getValue().equals(result.getValue()));

    }


    @Test
    public void TestLessThanStatementCompareNumberJSonFALSE(){
        JsonNumber n1 = new JsonNumber(4);
        JsonNumber n2 = new JsonNumber(3);

        Verdict expected = new Verdict(Verdict.Value.FALSE);

        Verdict result = ls.compare(n1,n2);

        assertTrue(expected.getValue().equals(result.getValue()));

    }


    @Test
    public void TestLessThanStatementCompareStringJSonNumericTRUE(){
        JsonString n1 = new JsonString("3");
        JsonString n2 = new JsonString("4");

        Verdict expected = new Verdict(Verdict.Value.TRUE);

        Verdict result = ls.compare(n1,n2);

        assertTrue(expected.getValue().equals(result.getValue()));

    }

    @Test
    public void TestLessThanStatementCompareStringJSonNumericFALSE(){
        JsonString n1 = new JsonString("4");
        JsonString n2 = new JsonString("3");

        Verdict expected = new Verdict(Verdict.Value.FALSE);

        Verdict result = ls.compare(n1,n2);

        assertTrue(expected.getValue().equals(result.getValue()));

    }


    @Test
    public void TestLessThanStatementCompareStringJSonNotNumericTRUE(){
        JsonString n1 = new JsonString("A");
        JsonString n2 = new JsonString("B");

        Verdict expected = new Verdict(Verdict.Value.TRUE);

        Verdict result = ls.compare(n1,n2);

        assertTrue(expected.getValue().equals(result.getValue()));

    }


    @Test
    public void TestLessThanStatementCompareStringJSonNotNumericFALSE(){
        JsonString n1 = new JsonString("B");
        JsonString n2 = new JsonString("A");

        Verdict expected = new Verdict(Verdict.Value.FALSE);

        Verdict result = ls.compare(n1,n2);

        assertTrue(expected.getValue().equals(result.getValue()));

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

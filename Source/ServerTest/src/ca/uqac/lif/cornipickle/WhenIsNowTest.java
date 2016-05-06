package ca.uqac.lif.cornipickle;/**
 * Created by paul on 03/05/16.
 */

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.cornipickle.server.CornipickleServer;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StacktracePrintingMatcher;
import org.junit.runners.model.*;

import static org.junit.Assert.*;

public class WhenIsNowTest {

    CornipickleParser parser;
    WhenIsNow win;
    JsonParser j_parser;
    JsonElement jse;

    @Before
    public void setUp() throws JsonParser.JsonParseException {

        parser = new CornipickleParser();

        String line = "When $d is now $y ( $d's width equals (200 + 100) )\n";

        ParseNode pn = shouldParseAndNotNull(line, "<when>");
        LanguageElement e = parser.parseStatement(pn);

        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof WhenIsNow))
        {
            fail("Got wrong type of object");
        }

        win= (WhenIsNow) e;


        j_parser = new JsonParser();
        String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-8.json");
        jse = j_parser.parse(json);
    }

    @Test
    public void WhenIsNowTestDefaultConstructor(){
        WhenIsNow win2 = new WhenIsNow();
        assertTrue(win2 instanceof Statement);
    }


    @Test
    public void WhenIsNowTestToString(){
        String expected = "When $d is now $y (\n$d's width equals 200.0 + 100.0)";
        assertTrue(expected.equals(win.toString()));
    }
    
    @Test
    public void WhenIsNowTestIsTemporal(){
        assertFalse(win.isTemporal());
    }

    @Test
    public void WhenIsNowTestGetClone(){
        WhenIsNow win2 = (WhenIsNow) win.getClone();
        boolean sameInnerStatement = win.m_innerStatement.toString().equals(win2.m_innerStatement.toString());
        boolean sameBefore = win.m_variableBefore.equals(win2.m_variableBefore);
        boolean sameAfter =win.m_variableAfter.equals(win2.m_variableAfter);
        assertTrue(sameInnerStatement&&sameAfter&&sameBefore);
    }

    @Test
    public void WhenIsNowTestResetHistory(){
        win.resetHistory();
        assertTrue(win.m_elementNow==null&&win.m_verdict.getValue()== Verdict.Value.INCONCLUSIVE);
    }


    /*@Test
    public void WhenIsNowTestFetchWithId() throws JsonParser.JsonParseException {
        JsonElement j = WhenIsNow.fetchWithId(jse, 1);
        System.out.println(j);

    }*/























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

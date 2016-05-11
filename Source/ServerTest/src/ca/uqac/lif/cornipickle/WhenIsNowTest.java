package ca.uqac.lif.cornipickle;/**
 * Created by paul on 03/05/16.
 */

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.cornipickle.server.CornipickleServer;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.*;
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


    @Test
    public void WhenIsNowTestFetchWithIdOK() throws JsonParser.JsonParseException {
        // Create "document"
        JsonList el = new JsonList();
        {
            JsonMap x = new JsonMap();
            x.put("width", new JsonNumber(100));
            x.put("tagname", new JsonString("p"));
            x.put("id", new JsonNumber(0));
            el.add(x);
        }
        {
            JsonMap x = new JsonMap();
            x.put("width", new JsonNumber(101));
            x.put("tagname", new JsonString("q"));
            x.put("id", new JsonNumber(1));
            el.add(x);
        }
        JsonMap main = new JsonMap();
        main.put("children", el);
        main.put("tagname", new JsonString("h1"));
        main.put("cornipickleid", new JsonNumber(1));



        JsonElement e =WhenIsNow.fetchWithId(main, 1);

        assertTrue(e.toString().equals(main.toString()));





    }


    @Test
    public void WhenIsNowTestFetchWithIdNonTrouve() throws JsonParser.JsonParseException {
        // Create "document"
        JsonList el = new JsonList();
        {
            JsonMap x = new JsonMap();
            x.put("width", new JsonNumber(100));
            x.put("tagname", new JsonString("p"));
            x.put("id", new JsonNumber(0));
            el.add(x);
        }
        {
            JsonMap x = new JsonMap();
            x.put("width", new JsonNumber(101));
            x.put("tagname", new JsonString("q"));
            x.put("id", new JsonNumber(1));
            el.add(x);
        }
        JsonMap main = new JsonMap();
        main.put("children", el);
        main.put("tagname", new JsonString("h1"));
        //main.put("cornipickleid", new JsonNumber(1));



        JsonElement e =WhenIsNow.fetchWithId(main, 1);

        assertTrue(e==null);

    }


    @Test
    public void WhenIsNowTestFetchWithIdNonMap(){
        JsonNumber jn = new JsonNumber(1);
        assertTrue(WhenIsNow.fetchWithId(jn, 1)==null);
    }


    @Test
    public void WhenIsNowTestFetchWithIdChildren(){
        // Create "document"
        JsonList el = new JsonList();
        {
            JsonMap x = new JsonMap();
            x.put("width", new JsonNumber(100));
            x.put("tagname", new JsonString("p"));
            x.put("id", new JsonNumber(0));
            x.put("cornipickleid", new JsonNumber(1));
            el.add(x);
        }
        {
            JsonMap x = new JsonMap();
            x.put("width", new JsonNumber(101));
            x.put("tagname", new JsonString("q"));
            x.put("id", new JsonNumber(1));
            el.add(x);
        }
        JsonMap main = new JsonMap();
        main.put("children", el);
        main.put("tagname", new JsonString("h1"));

        JsonElement e =WhenIsNow.fetchWithId(main, 1);

        boolean v1 = e.toString().contains("\"tagname\":\"p\"");
        boolean v2 = e.toString().contains("\"cornipickleid\":1");
        boolean v3 = e.toString().contains("\"width\":100");
        boolean v4 = e.toString().contains("\"id\":0");

        boolean f1 = !e.toString().contains("\"tagname\":\"q\"");

        assertTrue(v1&&v2&&v3&&v4&&f1);

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

package ca.uqac.lif.cornipickle;/**
 * Created by paul on 05/05/16.
 */

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

import org.junit.Before;
import org.junit.Test;

import static ca.uqac.lif.cornipickle.UtilsTest.shouldParseAndNotNull;
import static org.junit.Assert.*;

public class EventuallyTest {
    CornipickleParser parser;
    Eventually ev;

    @Before
    public void setUp() {
        parser = new CornipickleParser();

        String line = "Eventually ( \"3\" equals \"3\")\n";

        ParseNode pn = shouldParseAndNotNull(parser, line, "<eventually>");
        LanguageElement e = parser.parseStatement(pn);

        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof Eventually))
        {
            fail("Got wrong type of object");
        }

        ev = (Eventually)e;

    }
    
    
    @Test
    public void EventuallyTestToString(){
        String expected = "Eventually (\n3 equals 3\n)";
        assertTrue(expected.equals(ev.toString()));
    }

    @Test
    public void EventuallyTestGetClone(){
        Eventually ev2 = (Eventually)ev.getClone();
        assertTrue(ev.m_innerStatement.toString().equals(ev2.m_innerStatement.toString()));
    }
    
    @Test
    public void Eventuallytestevaluatetemporal(){
    	JsonElement je =new JsonNumber(3);
    	Map<String,JsonElement>map=new HashMap<String,JsonElement>();
    	assertTrue(ev.evaluateTemporal(je, map).m_value.equals(Verdict.Value.TRUE));
    }

}

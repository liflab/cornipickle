package ca.uqac.lif.cornipickle;/**
 * Created by paul on 05/05/16.
 */

import ca.uqac.lif.bullwinkle.ParseNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GloballyTest {

    CornipickleParser parser;
    Globally g;

    @Before
    public void setUp() {
        parser = new CornipickleParser();

        String line =  "Always ( \"3\" equals \"3\")\n";
        ParseNode pn = UtilsTest.shouldParseAndNotNull(parser, line, "<globally>");
        LanguageElement e = parser.parseStatement(pn);
        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof Globally))
        {
            fail("Got wrong type of object");
        }

        g = (Globally) e;
    }
    
    
    
    @Test
    public void GloballyTestConstructor(){
        Globally g2 = new Globally();
        boolean list = g2.m_inMonitors.isEmpty();
        boolean verdict = g2.m_verdict.getValue()==Verdict.Value.INCONCLUSIVE;
        assertTrue(verdict&&list);
    }

    @Test
    public void GloballyTestSetInnetStatement(){
        Statement s = new EqualsStatement();
        g.setInnerStatement(s);
        assertTrue(g.m_innerStatement.equals(s));
    }

    @Test
    public void GloballyTestResetHistory(){
        g.resetHistory();
        boolean list = g.m_inMonitors.isEmpty();
        boolean verdict = g.m_verdict.getValue()==Verdict.Value.INCONCLUSIVE;
        assertTrue(verdict&&list);
    }


    @Test
    public void GloballyTestToString(){
        String expected = "Always (\n3 equals 3\n)";
        assertTrue(expected.equals(g.toString()));
    }

    @Test
    public void GloballyTestGetClone(){
        Globally g2 = (Globally)g.getClone();
        assertTrue(g.m_innerStatement.toString().equals(g2.m_innerStatement.toString()));
    }

}

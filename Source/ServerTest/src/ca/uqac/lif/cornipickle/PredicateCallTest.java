package ca.uqac.lif.cornipickle;/**
 * Created by paul on 05/05/16.
 */

import org.junit.Before;
import org.junit.Test;

import javax.sql.rowset.Predicate;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class PredicateCallTest {

    PredicateDefinition pd;
    PredicateCall pc;
    @Before
    public void setUp() {
        pd = new PredicateDefinition(new StringConstant("rule"));
        pc = new PredicateCall(pd, "match", new LinkedList<String>());
        pc.m_predicate.m_predicate = new EqualsStatement();
    }
    
    @Test
    public void PredicateCallTestDefaultConstructor(){
        PredicateCall pc = new PredicateCall();
        assertTrue(pc instanceof PredicateCall);
    }
    
    @Test
    public void PredicateCallTestConstructor(){
        PredicateDefinition pd = new PredicateDefinition(new StringConstant("rule"));
        PredicateCall pc = new PredicateCall(pd, "match", new LinkedList<String>());

        boolean pred = pd.getRuleName().equals(pc.m_predicate.getRuleName());
        boolean match = "match".equals(pc.m_matchedString);
        boolean list = pc.m_captureBlocks.isEmpty();

        assertTrue(pred&&match&&list);
    }

    @Test
    public void PredicateCallTestGetMatchedString(){
        assertTrue("match".equals(pc.getMatchedString()));
    }
    
    @Test
    public void PredicateCallTestResetHistory(){
        pc.resetHistory();
        boolean isInconclusive = pc.m_verdict.is(Verdict.Value.INCONCLUSIVE);
        boolean predicateReset = pc.m_predicate.m_verdict.is(Verdict.Value.INCONCLUSIVE);
        assertTrue(isInconclusive&&predicateReset);
    }

    @Test
    public void PredicateCallTestGetPredicateDefinition(){
        PredicateDefinition pdExpected = new PredicateDefinition(new StringConstant("rule"));
        assertTrue(pdExpected.getRuleName().equals(pc.getPredicateDefinition().getRuleName()));
    }

    @Test
    public void PredicateCallTestToString(){
        String expected = "match";
        assertTrue(expected.equals(pc.toString()));
    }

    @Test
    public void PredicateCallTestIsTemporal(){
        assertFalse(pc.isTemporal());
    }
    
    /*@Test
    public void PredicateCallTestGetClone(){
        PredicateCall pc2 = pc.getClone();
        boolean predicate = pc2.m_predicate.toString().equals(pc.m_predicate.toString());
        boolean matched = pc2.getMatchedString().equals(pc.getMatchedString());
    }*/





}

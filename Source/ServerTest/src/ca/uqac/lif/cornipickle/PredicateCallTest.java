package ca.uqac.lif.cornipickle;/**
 * Created by paul on 05/05/16.
 */

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

import javax.sql.rowset.Predicate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PredicateCallTest {

    PredicateDefinition pd;
    PredicateCall pc;
    @Before
    public void setUp() {
        pd = new PredicateDefinition(new StringConstant("rule"));
        pd.m_predicate= new OrStatement();
        List l=new LinkedList<String>();
        pc = new PredicateCall(pd, "match", l);
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
    
    @Test
    public void PredicateCallTestGetClone(){        	
        PredicateCall pc2 =pc.getClone();
        assertTrue(pc2.m_predicate.m_predicate.toString().equals(pc.m_predicate.m_predicate.toString()));
    }
    @Test 
    public void testpostfixAccept(){
    	LanguageElementVisitor test =new AttributeExtractor();
    	pc.postfixAccept(test);
    	assertTrue(true);
    }
    @Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		pc.m_captureBlocks.setSize(5);
		pc.m_captureBlocks.set(0, "Cornipickle");
		pc.m_captureBlocks.set(1, "Cornipickle");
		pc.m_captureBlocks.set(2, "Cornipickle");
		pc.m_captureBlocks.set(3, "Cornipickle");
		pc.m_captureBlocks.set(4, "Cornipickle");
		pc.m_predicate.m_captureGroups.setSize(5);
		assertTrue(pc.evaluateAtemporal(je, test).m_value.equals(Verdict.Value.FALSE));		
	}
   @Test
	public void testEvaluateTemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		pc.m_captureBlocks.setSize(5);
		pc.m_captureBlocks.set(0, "Cornipickle");
		pc.m_captureBlocks.set(1, "Cornipickle");
		pc.m_captureBlocks.set(2, "Cornipickle");
		pc.m_captureBlocks.set(3, "Cornipickle");
		pc.m_captureBlocks.set(4, "Cornipickle");
		pc.m_predicate.m_captureGroups.setSize(5);
		assertTrue(pc.evaluateTemporal(je, test).m_value.equals(Verdict.Value.FALSE));		
	}





}

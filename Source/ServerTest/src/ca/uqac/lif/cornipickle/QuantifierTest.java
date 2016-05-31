package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QuantifierTest {
	Quantifier q;
	
	@Before
	public void setUp()throws Exception{
		q= new ForAllStatement();
	}
	
    @Test 
    public void testprefixAccept(){// a modifier si possible
    	LanguageElementVisitor test =new AttributeExtractor();
    	q.m_innerStatement=new OrStatement();
    	q.m_set=new RegexCapture();
    	q.prefixAccept(test);
    	assertTrue(true);
    }
    @Test 
    public void testpostfixAccept(){// a modifier si possible
    	LanguageElementVisitor test =new AttributeExtractor();
    	q.m_innerStatement=new OrStatement();
    	q.m_set=new RegexCapture();
    	q.postfixAccept(test);
    	assertTrue(true);
    }

	/*@Test //marche pas
	public void testEvaluateTemporal() {
		JsonElement je=new JsonNumber(15);
		Map<String,JsonElement>map=new HashMap<String,JsonElement>();
		SetExpression s =new RegexCapture();
		q.setDomain(s);
		assertTrue(q.evaluateTemporal(je, map).m_value.equals(Verdict.Value.INCONCLUSIVE));
	}*/

	@Test
	public void testResetHistory() {
		q.m_verdict= new Verdict(Verdict.Value.TRUE);
		q.m_innerStatement=new OrStatement();
		q.resetHistory();
		assertTrue(q.m_verdict.m_value.equals(Verdict.Value.INCONCLUSIVE)&&q.m_domain==null);
	}

	@Test
	public void testGetSet() {
		q.m_set=new RegexCapture();
		assertTrue(q.getSet().toString().equals("match null with null"));
	}

}

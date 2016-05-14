package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class NextTest {
	
	
	CornipickleParser parser;
	Next n;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();

	    String line = "Next (\"3\" is less than \"3\")\n";

	    ParseNode pn = shouldParseAndNotNull(line, "<next>");
	    LanguageElement e = parser.parseStatement(pn);

	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof Next))
	    {
	      fail("Got wrong type of object");
	    }
	    n=(Next)e;

	}
	
	@Test
	public void testEvaluateTemporal() {
		JsonElement je =new JsonNumber(3);
		Map<String,JsonElement>map=new HashMap<String,JsonElement>();
		assertTrue(n.evaluateTemporal(je, map).m_value.equals(Verdict.Value.INCONCLUSIVE));
	}
	@Test
	public void testEvaluateTemporal2() {
		JsonElement je =new JsonNumber(3);
		Map<String,JsonElement>map=new HashMap<String,JsonElement>();
		n.evaluateTemporal(je, map);
		assertTrue(n.evaluateTemporal(je, map).m_value.equals(Verdict.Value.FALSE));
	}
	@Test
	public void testEvaluateTemporal3() {
		JsonElement je =new JsonNumber(3);
		Map<String,JsonElement>map=new HashMap<String,JsonElement>();
		n.evaluateTemporal(je, map);
		n.evaluateTemporal(je, map);
		assertTrue(n.evaluateTemporal(je, map).m_value.equals(Verdict.Value.FALSE));
	}

	@Test
	public void testResetHistory() {
		n.m_firstEvent=false;
		n.resetHistory();
		assertTrue(n.m_verdict.m_value.equals(Verdict.Value.INCONCLUSIVE)&&n.m_firstEvent==true&&n.m_innerStatement.toString().equals("3 is less than 3"));
	}
	
	@Test
	public void testpostfixaccept(){
		LanguageElementVisitor lev =new AttributeExtractor();
		n.postfixAccept(lev);
		assertTrue(true);		
	}
	@Test
	public void testprefixaccept(){
		LanguageElementVisitor lev =new AttributeExtractor();
		n.prefixAccept(lev);
		assertTrue(true);		
	}
	
	public ParseNode shouldParseAndNotNull(String line, String start_symbol) {
		BnfParser p = parser.getParser();
		// p.setDebugMode(true);
		p.setStartRule(start_symbol);
		ParseNode pn = null;

		try {
			pn = p.parse(line);
		} catch (ParseException e) {
			fail(e.toString());
		}
		if (pn == null) {
			fail("Failed parsing expression through grammar: returned null");
		}
		return pn;
	}

}

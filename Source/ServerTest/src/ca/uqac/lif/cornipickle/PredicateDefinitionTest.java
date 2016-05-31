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

public class PredicateDefinitionTest {
	
	
	CornipickleParser parser;
	PredicateDefinition pd;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();
	    String line = "We say that all is good when (1 equals 1)";
	    ParseNode pn = shouldParseAndNotNull(line, "<predicate>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof PredicateDefinition))
	    {
	      fail("Got wrong type of object");
	    }
	    pd = (PredicateDefinition) e;

	}
	@Test
	public void testPostfixAccept() {
		LanguageElementVisitor lev= new AttributeExtractor();
		pd.postfixAccept(lev);
		assertTrue(true);
	}

	@Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		assertTrue(pd.evaluateAtemporal(je, test).m_value.equals(Verdict.Value.TRUE));	
	}

	@Test
	public void testEvaluateTemporal() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();
		assertTrue(pd.evaluateTemporal(je, test).m_value.equals(Verdict.Value.TRUE));	
	}

	@Test
	public void testGetPredicate() {
		assertTrue(pd.getPredicate().toString().equals("1.0 equals 1.0"));
	}

	@Test
	public void testGetCaptureGroup() {
		pd.m_captureGroups.add("Cornipickle");
		assertTrue(pd.getCaptureGroup(0).equals("Cornipickle"));
	}

	@Test
	public void testGetStatement() {
		assertTrue(pd.getStatement().toString().equals("1.0 equals 1.0"));
	}

	@Test
	public void testGetClone() {
		PredicateDefinition pd2;
		pd2=(PredicateDefinition)pd.getClone();		
		assertTrue(pd2.m_captureGroups.equals(pd.m_captureGroups) && pd2.m_predicate.toString().equals(pd.m_predicate.toString()));
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

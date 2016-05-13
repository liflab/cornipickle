package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

public class NextTimeTest {
	CornipickleParser parser;
	NextTime nt;

	@Before
	public void setUp() throws Exception {
		parser = new CornipickleParser();
		String line = "The next time (0 equals 0) Then (0 equals 0)";
		ParseNode pn = shouldParseAndNotNull(line, "<next_time>");
		LanguageElement e = parser.parseStatement(pn);
		if (e == null) {
			fail("Parsing returned null");
		}
		if (!(e instanceof NextTime)) {
			fail("Got wrong type of object");
		}
		nt = (NextTime) e;

	}
	@Test
	public void testToStringString() {
		assertTrue(nt.toString().equals("The next time (\n0.0 equals 0.0)\nThen (\n0.0 equals 0.0)"));
	
	}

	@Test
	public void testPrefixAccept() {
		LanguageElementVisitor test=new AttributeExtractor();
		nt.prefixAccept(test);
		assertTrue(true);
	}

	@Test
	public void testPostfixAccept() {
		LanguageElementVisitor test=new AttributeExtractor();
		nt.postfixAccept(test);
		assertTrue(true);
	}

	@Test
	public void testResetHistory() {
		nt.m_verdict.m_value=Verdict.Value.FALSE;		
		nt.resetHistory();
		assertTrue(nt.m_verdict.m_value.equals(Verdict.Value.INCONCLUSIVE));
	}

	@Test
	public void testGetClone() {
		NextTime nt2 =nt.getClone();
		assertTrue(nt2.toString().equals(nt.toString()));
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

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
import ca.uqac.lif.json.JsonString;

public class IsDefinedStatementTest {
	
	CornipickleParser parser;
	IsDefinedStatement ids;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();
	    String line = "$x's accesskey is defined";
	    ParseNode pn = shouldParseAndNotNull(line, "<defined>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof IsDefinedStatement))
	    {
	      fail("Got wrong type of object");
	    }
	    
	    ids=(IsDefinedStatement)e;

	}
	
	/*@Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je =new JsonString("test");
		Map<String,JsonElement> map =new HashMap<String,JsonElement>();	
		System.out.println(ids.m_property);
		assertTrue(((IsDefinedStatement)ids).evaluateAtemporal(je, map).equals(Verdict.Value.FALSE));
	}*/

	@Test
	public void testIsTemporal() {
		boolean test =ids.isTemporal();
		assertTrue(test ==false);
	}
	
	@Test
	public void testResetHistory() {
		ids.resetHistory();
		assertTrue(ids.m_verdict.toPlainString().equals("INCONCLUSIVE"));
	}

	@Test
	public void testCheck() {
		JsonElement je =new JsonString("undefined");
		assertTrue(ids.check(je).getValue().equals(Verdict.Value.FALSE));
	}

	@Test
	public void testGetKeyword() {
		assertTrue(ids.getKeyword().equals("is defined"));
	}

	@Test
	public void testGetClone() {
		IsDefinedStatement ids2;
		ids2 =ids.getClone();
		assertTrue(ids2.toString().equals(ids.toString()));
	}
	@Test
	public void testPostfixAccept() {
		LanguageElementVisitor lev= new AttributeExtractor();
		ids.postfixAccept(lev);
		assertTrue(true);
	}
	@Test
	public void testPrefixAccept() {
		LanguageElementVisitor lev= new AttributeExtractor();
		ids.prefixAccept(lev);
		assertTrue(true);
	}


	
	  public ParseNode shouldParseAndNotNull(String line, String start_symbol){
		    BnfParser p = parser.getParser();
		    //p.setDebugMode(true);
		    p.setStartRule(start_symbol);
		    ParseNode pn = null;

		    try
		    {
		      pn = p.parse(line);
		    } catch (ParseException e)
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

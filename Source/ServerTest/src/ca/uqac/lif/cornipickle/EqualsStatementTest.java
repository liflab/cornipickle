package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.json.JsonString;

public class EqualsStatementTest {
	
	CornipickleParser parser;
	EqualsStatement es;
	
	@Before
	public void setUp() throws Exception {
		parser =new CornipickleParser();
	    String line = "\"3\" equals \"3\"";
	    ParseNode pn = shouldParseAndNotNull(line, "<equality>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof EqualsStatement))
	    {
	      fail("Got wrong type of object");
	    }
	    es = (EqualsStatement)e;
	}

	@Test
	public void testToStringString() {
		assertTrue(es.toString().equals("3 equals 3"));
	}

	@Test
	public void testCompareJsonStringJsonString() {
		JsonString jn1=new JsonString("A");
		JsonString jn2=new JsonString("A");
		assertTrue(es.compare(jn1, jn2).getValue().equals(Verdict.Value.TRUE));
	}
	@Test
	public void testCompareJsonStringJsonString2() {
		JsonString jn1=new JsonString("A");
		JsonString jn2=new JsonString("B");
		assertTrue(es.compare(jn1, jn2).getValue().equals(Verdict.Value.FALSE));
	}
	@Test
	public void testCompareJsonStringJsonString3() {
		JsonString jn1=new JsonString("A");
		JsonString jn2=new JsonString("3");
		assertTrue(es.compare(jn1, jn2).getValue().equals(Verdict.Value.FALSE));
	}
	@Test
	public void testCompareJsonStringJsonString4() {
		JsonString jn1=new JsonString("3");
		JsonString jn2=new JsonString("3");
		assertTrue(es.compare(jn1, jn2).getValue().equals(Verdict.Value.TRUE));
	}
	@Test
	public void testCompareJsonStringJsonString5() {
		JsonString jn1=new JsonString("3");
		JsonString jn2=new JsonString("A");
		assertTrue(es.compare(jn1, jn2).getValue().equals(Verdict.Value.FALSE));
	}

	@Test
	public void testCompareJsonNumberJsonNumber() {
		JsonString jn1=new JsonString("3");
		JsonString jn2=new JsonString("3");
		assertTrue(es.compare(jn1, jn2).getValue().equals(Verdict.Value.TRUE));
	}
	

	@Test
	public void testGetKeyword() {
		assertTrue(es.getKeyword().equals("equals"));
	}

	@Test
	public void testGetClone() {
		EqualsStatement es2;
		es2=es.getClone();
		assertTrue(es2.toString().equals(es.toString()));
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

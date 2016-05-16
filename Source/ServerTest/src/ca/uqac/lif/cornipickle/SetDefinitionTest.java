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
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;

public class SetDefinitionTest {
	
	CornipickleParser parser;
	SetDefinition sd;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();
	    String line = "A tomato is any of \"abc\"";
	    ParseNode pn = shouldParseAndNotNull(line, "<def_set>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof SetDefinition))
	    {
	      fail("Got wrong type of object");
	    }
	    sd=(SetDefinition)e;

	}
	@Test
	public void testToStringString() {
		assertTrue(((SetDefinition)sd).toString("tomato").equals("tomato"));
	}

	@Test
	public void testEvaluateJsonElementMapOfStringJsonElement() {
		JsonElement je =new JsonList();		
		Map<String, JsonElement>map=new HashMap<String,JsonElement>();
		assertTrue(((SetDefinition)sd).evaluate(je, map).get(0).toString().equals("\"abc\""));
	}
	/*@Test
	public void testEvaluateJsonElementMapOfStringJsonElement2() {
		JsonElement je =new JsonNumber(3);		
		Map<String, JsonElement>map=new HashMap<String,JsonElement>();
		assertTrue(((SetDefinition)sd).evaluate(je, map).equals(null));
	}*/

	@Test
	public void testGetClone() {
		SetDefinition sd2;
		sd2=(SetDefinition)sd.getClone();
		assertTrue(sd2.m_setName.toString().equals(sd.m_setName.toString()));
	}

	@Test
	public void testSetDefinition() {
		SetDefinition sd2=new SetDefinition();
		assertTrue(sd2 instanceof SetDefinition);
	}

	@Test
	public void testSetDefinitionStringConstant() {
		StringConstant sc =new StringConstant("Cornipickle");
		SetDefinition sd2=new SetDefinition(sc);
		assertTrue(sd2.m_setName.toString().equals("Cornipickle"));
	}

	@Test
	public void testSetDefinitionString() {
		String s ="cornipickle";
		SetDefinition sd2=new SetDefinition(s);
		assertTrue(sd2.m_setName.toString().equals("cornipickle"));
	}

	@Test
	public void testGetName() {
		assertTrue(sd.getName().equals("tomato"));
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

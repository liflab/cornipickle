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
import ca.uqac.lif.json.JsonString;

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
	public void testToStringString() {//not good go to 
		SetDefinition sd2 =new SetDefinition();
		StringConstant sc =new StringConstant("Cornipickle");
		sd2.m_setName=sc;
		assertTrue(((SetDefinition)sd2).toString("cornipickle").equals("cornipickleCornipickle"));
	}

	@Test
	public void testEvaluateJsonElementMapOfStringJsonElement() {
		SetDefinition sd2 =new SetDefinition();
		StringConstant sc =new StringConstant("Cornipickle");
		sd2.m_setName=sc;
		JsonString js =new JsonString("Cornipickle");
		JsonList je =new JsonList();
		je.add(js);
		Map<String, JsonElement>map=new HashMap<String,JsonElement>();
		map.put("Cornipickle", je);
		assertTrue(((SetDefinition)sd2).evaluate(je, map).toString().equals("[\"Cornipickle\"]"));
	}
	@Test
	public void testEvaluateJsonElementMapOfStringJsonElement2() {
		SetDefinition sd2 =new SetDefinition();
		StringConstant sc =new StringConstant("Cornipickle");
		sd2.m_setName=sc;
		JsonString js =new JsonString("Cornipickle");
		Map<String, JsonElement>map=new HashMap<String,JsonElement>();
		map.put("Cornipickle", js);
		assertTrue(((SetDefinition)sd2).evaluate(js, map)==null);
	}


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

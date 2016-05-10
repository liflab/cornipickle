package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;

public class RegexMatchTest {
	CornipickleParser parser;
	RegexMatch rm;
	@Before
	public void setUp() throws Exception {
		parser= new CornipickleParser();
		String line = "$x's disabled matches \"true\"";
	    ParseNode pn = shouldParseAndNotNull(line, "<regex_match>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof RegexMatch))
	    {
	      fail("Got wrong type of object");
	    }
	    rm=(RegexMatch)e;
	}

	@Test
	public void testToStringString() {
		assertTrue(rm.toString().equals("$x's disabled matches true"));
	}

	@Test
	public void testCompareJsonStringJsonString() {//compare two string js1 and js2
		JsonString js1=new JsonString("#aaaaaa");
		JsonString js2=new JsonString("aaaaaa");
		assertTrue(rm.compare(js1,js2).getValue().equals(Verdict.Value.TRUE));
	}

	@Test
	public void testCompareJsonNumberJsonString2() {
		JsonString js1=new JsonString("A");
		JsonString js2=new JsonString("B");
		assertTrue(rm.compare(js1,js2).getValue().equals(Verdict.Value.FALSE));
	}


	@Test
	public void testCompareJsonNumberJsonNumber() {
		JsonNumber js1=new JsonNumber(3);
		JsonNumber js2=new JsonNumber(3);
		assertTrue(rm.compare(js1,js2).getValue().equals(Verdict.Value.FALSE));
	}

	@Test
	public void testGetKeyword() {
		assertTrue(rm.getKeyword().toString().equals("matches"));
	}

	@Test
	public void testGetClone() {
		RegexMatch rm2;
		rm2=rm.getClone();
		assertTrue(rm2.toString().equals(rm.toString()));
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

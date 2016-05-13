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
import ca.uqac.lif.json.JsonString;

public class MultOperationTest {
	CornipickleParser parser;
	MultOperation mo;
	@Before
	public void setUp() throws Exception {
		parser =new CornipickleParser();
		String line = "(200 * 100)";
	    ParseNode pn = shouldParseAndNotNull(line, "<mult>");
	    LanguageElement e = parser.parseStatement(pn);
	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof MultOperation))
	    {
	      fail("Got wrong type of object");
	    }
	    mo=(MultOperation)e;
	}

	@Test
	public void testToStringString() {
		assertTrue(mo.toString().equals("200.0 * 100.0"));
	}

	@Test
	public void testEvaluateJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();			
		assertTrue(mo.evaluate(je, test).toString().equals("20000"));		
	}

	@Test
	public void testMultOperation() {
		MultOperation mo2=new MultOperation();
		assertTrue(mo2 instanceof Operation);
	}

	/*@Test
	public void testMultOperationPropertyProperty() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testGetClone() {
		MultOperation mo2;
		mo2=mo.getClone();
		assertTrue(mo2.toString().equals(mo.toString()));
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

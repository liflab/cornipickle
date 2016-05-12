package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

public class AttributeExtractorTest {
	
	CornipickleParser parser;
	AttributeExtractor ae;
	SubOperation so;
	@Before
	public void setUp() throws Exception {
		parser=new CornipickleParser();

	    String line = "(4-3)\n";

	    ParseNode pn = shouldParseAndNotNull(line, "<sub>");
	    LanguageElement e = parser.parseStatement(pn);

	    if (e == null)
	    {
	      fail("Parsing returned null");
	    }
	    if (!(e instanceof SubOperation))
	    {
	      fail("Got wrong type of object");
	    }
	    so=(SubOperation)e;
	    ae=new AttributeExtractor();

	}
	
	@Test
	public void testGetAttributes() {
		LanguageElement cs =new CssSelector("#Cornipickle");
		assertTrue(ae.getAttributes(cs).toString().equals("[id]"));
	}
	@Test
	public void testGetAttributes2() {
		LanguageElement cs =new CssSelector(".Cornipickle");
		assertTrue(ae.getAttributes(cs).toString().equals("[class]"));
	}
	@Test
	public void testGetAttributes3() {
		LanguageElement cs =new ElementPropertyPossessive();
		assertTrue(ae.getAttributes(cs).toString().equals("[null]"));
	}
	/*@Test
	public void testGetAttributes4() {
		List<String> s =new Vector<String>();
		LanguageElement cs =new PredicateCall(new PredicateDefinition(), "cornipickle",s);		
		assertTrue(ae.getAttributes(cs).toString().equals(""));
	}*/

	@Test
	public void testPop() {
		ae.pop();
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

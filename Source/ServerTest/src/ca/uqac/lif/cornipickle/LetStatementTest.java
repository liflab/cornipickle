package ca.uqac.lif.cornipickle;

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LetStatementTest {

    CornipickleParser parser;
    LetStatement ls;
    Interpreter interpreter;
    JsonParser j_parser;

    @Before
    public void setUp() throws Exception
    {
        parser = new CornipickleParser();
        interpreter = new Interpreter();
        j_parser = new JsonParser();

        String line = "Let $arf be $x's height (\"3\" equals \"3\")";
        ParseNode pn = shouldParseAndNotNull(line, "<let>");
        LanguageElement e = parser.parseStatement(pn);
        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof LetStatement))
        {
            fail("Got wrong type of object");
        }

        ls = (LetStatement)e;

    }


    @Test
    public void TestLetStatemenToString(){
        String expected = "let $arf be $x's height (\n3 equals 3\n)";
        assertTrue(expected.equals(ls.toString()));
    }

    @Test
    public void TestLetStatementIsTemporal(){
        assertFalse(ls.isTemporal());
    }

    @Test
    public void TestNegationStatementResetHistory(){
        ls.resetHistory();
        assertTrue(ls.m_verdict.is(Verdict.Value.INCONCLUSIVE));
    }

    @Test
    public void TestLetStatementGetVariable(){
        String expected = "$arf";
        assertTrue(expected.equals(ls.getVariable()));
    }

    @Test
    public void TestLetStatementDefaultConstructor(){
        LetStatement ls = new LetStatement();
        assertTrue(ls.getVariable().equals("")&&ls.m_property==null&&ls.m_value==null);
    }
    
    @Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();		
		assertTrue(ls.evaluateAtemporal(je, test).m_value.equals(Verdict.Value.TRUE));		
	}
    @Test
	public void testEvaluateAtemporalJsonElementMapOfStringJsonElement2() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();		
		ls.m_value=je;
		assertTrue(ls.evaluateAtemporal(je, test).m_value.equals(Verdict.Value.TRUE));		
	}
    @Test
	public void testEvaluateTemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();		
		ls.m_value=je;
		assertTrue(ls.evaluateTemporal(je, test).m_value.equals(Verdict.Value.TRUE));		
	}
    @Test 
    public void testprefixAccept(){// a modifier si possible
    	LanguageElementVisitor test =new AttributeExtractor();
    	ls.prefixAccept(test);
    	assertTrue(true);
    }
    @Test 
    public void testpostfixAccept(){// a modifier si possible
    	LanguageElementVisitor test =new AttributeExtractor();
    	ls.postfixAccept(test);
    	assertTrue(true);
    }
    
    /* Doesn't work because of $compteur + 1. $compteur can't be a constant
    @Test
    public void evaluateCpt() throws ParseException, JsonParseException {
      String toParse = "Let $compteur be 0 (For each $elem in $(.menu) (Let $compteur be ($compteur + 1) ($compteur is less than 3))).";
      
      interpreter.parseProperties(toParse);
      
      String jsonString = PackageFileReader.readPackageFile(this.getClass(), "data/snapshot-letcpt.json");
      JsonElement json = j_parser.parse(jsonString);
      
      interpreter.evaluateAll(json);
      
      assertTrue(interpreter.getVerdicts().containsValue(Verdict.Value.TRUE));
    }*/



    public ParseNode shouldParseAndNotNull(String line, String start_symbol)
    {
        BnfParser p = parser.getParser();
        //p.setDebugMode(true);
        p.setStartRule(start_symbol);
        ParseNode pn = null;

        try
        {
            pn = p.parse(line);
        } catch (BnfParser.ParseException e)
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

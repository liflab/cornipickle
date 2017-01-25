package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;
import ca.uqac.lif.json.JsonString;

public class EqualsStatementTest {
	
	CornipickleParser parser;
	EqualsStatement es;
	JsonParser jparser;
	
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
	    jparser = new JsonParser();
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
	
	@Test
	public void testEqualAndGlobally() throws JsonParseException, CornipickleParser.ParseException
	{
	  Interpreter interpreter = new Interpreter();
    String properties = PackageFileReader.readPackageFile(this.getClass(), "data/propertiesEG.txt");
    interpreter.parseProperties(properties);
    
    //First event
    JsonElement ev1 = jparser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/snapshot1.json"));
    interpreter.evaluateAll(ev1);
    
    assertTrue(interpreter.getVerdicts().size() == 1);
    
    for(Map.Entry<StatementMetadata, Verdict> entry : interpreter.getVerdicts().entrySet()) {
      assertTrue(entry.getValue().getValue().equals(Verdict.Value.INCONCLUSIVE));
    }
    
    //Second event
    JsonElement ev2 = jparser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/snapshot1.json"));
    interpreter.evaluateAll(ev2);
    
    assertTrue(interpreter.getVerdicts().size() == 1);
    
    for(Map.Entry<StatementMetadata, Verdict> entry : interpreter.getVerdicts().entrySet()) {
      assertTrue(entry.getValue().getValue().equals(Verdict.Value.INCONCLUSIVE));
    }
    
    //Third event
    JsonElement ev3 = jparser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/snapshot2.json"));
    interpreter.evaluateAll(ev3);
    
    assertTrue(interpreter.getVerdicts().size() == 1);
    
    for(Map.Entry<StatementMetadata, Verdict> entry : interpreter.getVerdicts().entrySet()) {
      assertTrue(entry.getValue().getValue().equals(Verdict.Value.FALSE));
    }
	}
}

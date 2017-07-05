package ca.uqac.lif.cornipickle;
import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WhenIsNowTest {

  CornipickleParser parser;
  WhenIsNow win;
  JsonParser j_parser;
  JsonElement jse;

  @Before
  public void setUp() throws JsonParser.JsonParseException {

    parser = new CornipickleParser();

    String line = "When $d is now $y ( $d's width equals (200 + 100) )\n";

    ParseNode pn = shouldParseAndNotNull(line, "<when>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof WhenIsNow))
    {
      fail("Got wrong type of object");
    }

    win= (WhenIsNow) e;


    j_parser = new JsonParser();
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-8.json");
    jse = j_parser.parse(json);
  }

  @Test
  public void WhenIsNowTestDefaultConstructor(){
    WhenIsNow win2 = new WhenIsNow();
    assertTrue(win2 instanceof Statement);
  }


  @Test
  public void WhenIsNowTestToString(){
    String expected = "When $d is now $y (\n$d's width equals 200.0 + 100.0)";
    assertTrue(expected.equals(win.toString()));
  }
  
  @Test
  public void WhenIsNowTestIsTemporal(){
    assertFalse(win.isTemporal());
  }

  @Test
  public void WhenIsNowTestGetClone(){
    WhenIsNow win2 = (WhenIsNow) win.getClone();
    boolean sameInnerStatement = win.m_innerStatement.toString().equals(win2.m_innerStatement.toString());
    boolean sameBefore = win.m_variableBefore.equals(win2.m_variableBefore);
    boolean sameAfter =win.m_variableAfter.equals(win2.m_variableAfter);
    assertTrue(sameInnerStatement&&sameAfter&&sameBefore);
  }

  @Test
  public void WhenIsNowTestResetHistory(){
    win.resetHistory();
    assertTrue(win.m_elementNow==null&&win.m_verdict.getValue()== Verdict.Value.INCONCLUSIVE);
  }


  @Test
  public void WhenIsNowTestFetchWithIdOK() throws JsonParser.JsonParseException {
    // Create "document"
    JsonList el = new JsonList();
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      x.put("id", new JsonNumber(0));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(101));
      x.put("tagname", new JsonString("q"));
      x.put("id", new JsonNumber(1));
      el.add(x);
    }
    JsonMap main = new JsonMap();
    main.put("children", el);
    main.put("tagname", new JsonString("h1"));
    main.put("cornipickleid", new JsonNumber(1));



    JsonElement e =WhenIsNow.fetchWithId(main, 1);

    assertTrue(e.toString().equals(main.toString()));
  }


  @Test
  public void WhenIsNowTestFetchWithIdNonTrouve() throws JsonParser.JsonParseException {
    // Create "document"
    JsonList el = new JsonList();
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      x.put("id", new JsonNumber(0));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(101));
      x.put("tagname", new JsonString("q"));
      x.put("id", new JsonNumber(1));
      el.add(x);
    }
    JsonMap main = new JsonMap();
    main.put("children", el);
    main.put("tagname", new JsonString("h1"));
    //main.put("cornipickleid", new JsonNumber(1));



    JsonElement e =WhenIsNow.fetchWithId(main, 1);

    assertTrue(e==null);

  }


  @Test
  public void WhenIsNowTestFetchWithIdNonMap(){
    JsonNumber jn = new JsonNumber(1);
    assertTrue(WhenIsNow.fetchWithId(jn, 1)==null);
  }


  @Test
  public void WhenIsNowTestFetchWithIdChildren(){
    // Create "document"
    JsonList el = new JsonList();
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      x.put("id", new JsonNumber(0));
      x.put("cornipickleid", new JsonNumber(1));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(101));
      x.put("tagname", new JsonString("q"));
      x.put("id", new JsonNumber(1));
      el.add(x);
    }
    JsonMap main = new JsonMap();
    main.put("children", el);
    main.put("tagname", new JsonString("h1"));

    JsonElement e =WhenIsNow.fetchWithId(main, 1);        

    boolean v1 = e.toString().contains("\"tagname\":\"p\"");
    boolean v2 = e.toString().contains("\"cornipickleid\":1");
    boolean v3 = e.toString().contains("\"width\":100");
    boolean v4 = e.toString().contains("\"id\":0");

    boolean f1 = !e.toString().contains("\"tagname\":\"q\"");

    assertTrue(v1&&v2&&v3&&v4&&f1);

  }
  
  @Test
  public void testEvaluateAtemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();		
		win.m_elementNow=je;
		assertTrue(win.evaluateAtemporal(je, test).m_value.equals(Verdict.Value.FALSE));		
  }
  @Test
  public void testEvaluateTemporalJsonElementMapOfStringJsonElement() {
		JsonElement je= new JsonNumber(15);	
		Map<String,JsonElement> test = new HashMap<String, JsonElement>();		
		win.m_elementNow=je;
		assertTrue(win.evaluateTemporal(je, test).m_value.equals(Verdict.Value.FALSE));		
  }
  
  @Test
  public void testEvaluateTemporal()
  { 
    String line = "Always ( There exists $x in $(body) such that "
        + "( Next ( When $x is now $y ( If ($x's width equals 200) Then ( $y's width equals 300 ) ) ) ) )";
    
    ParseNode pn = shouldParseAndNotNull(line, "<Always>");
    LanguageElement e = parser.parseStatement(pn);
    
    Globally g = (Globally)e;
  
    JsonMap snapshot1 = new JsonMap();
    snapshot1.put("tagname", "body");
    snapshot1.put("cornipickleid", 0);
    snapshot1.put("width", 200);
    
    Map<String, JsonElement> newd = new HashMap<String,JsonElement>();
    
    Verdict v = g.evaluateTemporal(snapshot1, newd);
    
    assertEquals(Verdict.Value.INCONCLUSIVE, v.m_value);
    
    JsonMap snapshot2 = new JsonMap();
    snapshot2.put("tagname", "body");
    snapshot2.put("cornipickleid", 0);
    snapshot2.put("width", 250);
    
    Map<String, JsonElement> newd2 = new HashMap<String,JsonElement>();
    
    v = g.evaluateTemporal(snapshot2, newd2);
    
    assertEquals(v.m_value, Verdict.Value.FALSE);
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
  
  @Test 
  public void testprefixAccept(){// a modifier si possible
  	LanguageElementVisitor test =new AttributeExtractor();
  	win.prefixAccept(test);
  	assertTrue(true);
  }
  
  @Test 
  public void testpostfixAccept(){// a modifier si possible
  	LanguageElementVisitor test =new AttributeExtractor();
  	win.postfixAccept(test);
  	assertTrue(true);
  }


}

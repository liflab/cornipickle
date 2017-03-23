package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class SetPropertyTest {
  
  Interpreter m_interpreter;
  JsonParser m_jparser;

  @Before
  public void setUp(){
      m_interpreter = new Interpreter();
      m_jparser = new JsonParser();
  }
  
  @Test
  public void testParse() throws ParseException
  {
    String toParse = "$(.menu)'s size is greater than 6.";
    
    m_interpreter.parseProperties(toParse);
    
    assertTrue(m_interpreter.getTagNames().contains(".menu"));
  }
  
  @Test
  public void testEval() throws ParseException, JsonParseException
  {
    String toParse = "$(.menu)'s size is less than 3.";
    
    m_interpreter.parseProperties(toParse);
    
    assertTrue(m_interpreter.getTagNames().contains(".menu"));
    
    String jsonString = PackageFileReader.readPackageFile(this.getClass(), "data/snapshot-letcpt.json");
    JsonElement json = m_jparser.parse(jsonString);
    
    m_interpreter.evaluateAll(json);
    
    for (Verdict verdict : m_interpreter.getVerdicts().values()) {
      assertEquals(verdict.getValue(), Verdict.Value.TRUE);
    }
    
    m_interpreter.clear();
    
    m_interpreter.parseProperties(toParse);
    
    assertTrue(m_interpreter.getTagNames().contains(".menu"));
    
    jsonString = PackageFileReader.readPackageFile(this.getClass(), "data/snapshot-letcpt2.json");
    json = m_jparser.parse(jsonString);
    
    m_interpreter.evaluateAll(json);
    
    for (Verdict verdict : m_interpreter.getVerdicts().values()) {
      assertEquals(verdict.getValue(), Verdict.Value.FALSE);
    }
  }

}

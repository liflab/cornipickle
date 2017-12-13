package ca.uqac.lif.cornipickle;

import static org.junit.Assert.assertTrue;

import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.transformations.CorniTransformation;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class ComparisonStatementTest
{
  
  Interpreter m_interpreter;
  JsonParser m_parser;
  
  @Before
  public void setUp()
  {
    m_interpreter = new Interpreter();
    m_parser = new JsonParser();
  }
  
  @Test
  public void flushTransformationsTest()
  {
    ComparisonStatement s = new EqualsStatement();
    
    s.setLeft(new ElementPropertyPossessive("$x", "bottom"));
    s.setRight(new NumberConstant(4));
    
    JsonMap element = new JsonMap();
    element.put("cornipickleid", new JsonNumber(1));
    element.put("bottom", new JsonNumber(5));
    
    JsonMap d = new JsonMap();
    d.put("$x", element);
    
    s.evaluateTemporal(new JsonMap(), d);
    
    assertTrue(s.getTransformations().size() > 0);
    
    Set<CorniTransformation> trans = s.flushTransformations();
    
    assertTrue(trans.size() > 0);
    assertTrue(s.getTransformations().size() == 0);
  }
  
  @Test
  public void serializeTransformationsTest()
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json");

    try
    {
      JsonElement page = m_parser.parse(json);
      m_interpreter.parseProperties("There exists $x in $(div>a) such that ( $x's bottom is 14 ).");
      m_interpreter.evaluateAll(page);
      TransformationBuilder builder = new TransformationBuilder(page);
      
      String memento = m_interpreter.saveToMemento();
      m_interpreter = m_interpreter.restoreFromMemento(memento);
      
      for(Entry<StatementMetadata,Statement> entry : m_interpreter.m_statements.entrySet())
      {
        entry.getValue().postfixAccept(builder);
      }
      
      assertTrue(builder.getTransformations().size() == 0);
      
    } catch (ParseException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonParseException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
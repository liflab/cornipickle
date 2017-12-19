package ca.uqac.lif.cornipickle;

import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class TransformationBuilderTest
{
  protected JsonParser s_parser = new JsonParser();
  protected Interpreter m_interpreter;
  
  @Before
  public void setUp() {
      m_interpreter = new Interpreter();
  }
  
  @Test
  public void visit()
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json");
    try {
      JsonElement page = s_parser.parse(json);
      m_interpreter.parseProperties("There exists $x in $(div>a) such that ( $x's bottom is 14 ).");
      TransformationBuilder builder = new TransformationBuilder(page);
      
      m_interpreter.cleanTransformations();
      
      m_interpreter.evaluateAll(page);
      
      for(Entry<StatementMetadata,Verdict> entry: m_interpreter.getVerdicts().entrySet())
      {
        if(entry.getValue().is(Verdict.Value.FALSE))
        {
          Statement s = m_interpreter.getProperty(entry.getKey());
          s.postfixAccept(builder);
        }
      }
      
      if(builder.getTransformations().size() != 6)
      {
        assert false;
      }
    } catch (JsonParseException | ParseException e) {
      assert false;
    }
  }

  @Test
  public void fetchPageIdsTest()
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json");
    try {
      JsonElement page = s_parser.parse(json);
      //m_interpreter.parseProperties("There exists $x in $(div>a) such that ( $x's bottom is 12 ).");
      TransformationBuilder builder = new TransformationBuilder(page);
      
      Set<Integer> ids = builder.getIds();
      if(ids.contains(1) && ids.contains(2) && ids.contains(3) && ids.contains(4))
      {
        assert true;
      }
      else
      {
        assert false;
      }
    } catch (JsonParseException e) {
      assert false;
    }
    
  }
}

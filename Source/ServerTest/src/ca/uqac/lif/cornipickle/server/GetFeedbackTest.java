package ca.uqac.lif.cornipickle.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.ElementFilter;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Statement;
import ca.uqac.lif.cornipickle.TransformationBuilder;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.faultfinder.FaultIterator;
import ca.uqac.lif.cornipickle.faultfinder.PositiveFaultIterator;
import ca.uqac.lif.cornipickle.transformations.CorniTransformation;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class GetFeedbackTest {

	protected JsonParser s_parser = new JsonParser();
	protected Interpreter m_interpreter;
	protected Map<StatementMetadata,FaultIterator<JsonElement>> m_faultIterators = new HashMap<StatementMetadata,FaultIterator<JsonElement>>();
  
	@Before
	public void setUp() {
		m_interpreter = new Interpreter();
	}
	
	@Test
	public void feedbackTest()
	{
	  String json = PackageFileReader.readPackageFile(this.getClass(), "../data/sample-12.json");
    
    JsonElement j = null;
  
    try 
    {
      j = s_parser.parse(json);
      m_interpreter.parseProperties("There exists $x in $(div>a) such that ( $x's bottom is 14 ).");
      TransformationBuilder builder = new TransformationBuilder(j);
      Set<StatementMetadata> statements = new HashSet<StatementMetadata>();
      m_interpreter.evaluateAll(j);
      for(Entry<StatementMetadata,Verdict> entry: m_interpreter.getVerdicts().entrySet())
      {
        if(entry.getValue().is(Verdict.Value.FALSE))
        {
          Statement s = m_interpreter.getProperty(entry.getKey());
          s.postfixAccept(builder);
          statements.add(entry.getKey());
        }
      }
 
      Set<CorniTransformation>  transfos = builder.getTransformations();
      JsonMap result = new JsonMap();
      for(StatementMetadata s : statements)
      {
        FaultIterator<JsonElement> faultIterator = new PositiveFaultIterator<JsonElement>(m_interpreter.getProperty(s), j, transfos, new ElementFilter());
        faultIterator.setTimeout(20000);
        if(faultIterator.hasNext())
        {
          m_faultIterators.put(s, faultIterator);
        }
      }
 
      for(Entry<StatementMetadata,FaultIterator<JsonElement>> entry : m_faultIterators.entrySet())
      {
        JsonList candidates = new JsonList();
        do
        {
          JsonList transformations = new JsonList();
          Set<? extends CorniTransformation> set = (Set<? extends CorniTransformation>) entry.getValue().next();
          
          for (CorniTransformation ct : set)
          {
            transformations.add(ct.toJson());
          }
          candidates.add(transformations);
        } while(entry.getValue().hasNext());
        result.put(entry.getKey().toString(), candidates);
      }
 
      if(!(result.values().size() == 1))
      {
        assert false;
      }
 
      for(Entry<String, JsonElement> entry : result.entrySet())
      {
        JsonList candidates = (JsonList)entry.getValue();
        if(!(candidates.size() == 2))
        {
          assert false;
        }
      }
    } catch (JsonParseException | ParseException e) {
       assert false; //Never supposed to happen....
    }
	}
}

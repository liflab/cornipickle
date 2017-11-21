package ca.uqac.lif.cornipickle.server;

import java.util.HashSet;
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
import ca.uqac.lif.cornipickle.faultfinder.NegativeFaultIterator;
import ca.uqac.lif.cornipickle.faultfinder.PositiveFaultIterator;
import ca.uqac.lif.cornipickle.transformations.CorniTransformation;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class GetFeedbackTest {

	protected JsonParser s_parser = new JsonParser();
	protected Interpreter m_interpreter;
	protected Set<FaultIterator<JsonElement>> m_faultIterators = new HashSet<FaultIterator<JsonElement>>();
  
	@Before
	public void setUp() {
		m_interpreter = new Interpreter();
	}
	
	@Test
	public void feedbackTest()
	{
		CallbackResponse c = process();
		assert false;
	}
	
	public CallbackResponse process()
	  {
  		String json = PackageFileReader.readPackageFile(this.getClass(), "../data/sample-12.json");
  		
  		JsonElement j = null;
  		boolean hasNext = false;
		
	    try 
	    {
	    	 j = s_parser.parse(json);
	    	 m_interpreter.parseProperties("There exists $x in $(div>a) such that ( $x's bottom is 14 ).");
	    	 TransformationBuilder builder = new TransformationBuilder(j);
	    	 Set<Statement> statements = new HashSet<Statement>();
	    	 m_interpreter.evaluateAll(j);
	    	 for(Entry<StatementMetadata,Verdict> entry: m_interpreter.getVerdicts().entrySet())
	         {
	           if(entry.getValue().is(Verdict.Value.FALSE))
	           {
	             Statement s = m_interpreter.getProperty(entry.getKey());
	             s.postfixAccept(builder);
	             statements.add(s);
	           }
	         }
	    	 
	    	 Set<CorniTransformation>  transfos = builder.getTransformations();
	    	 for(Statement statement : statements)
	    	 {
	    		 FaultIterator<JsonElement> m_faultIterator = new PositiveFaultIterator<JsonElement>(statement, j, transfos, new ElementFilter());
	    		 m_faultIterator.setTimeout(200000000);
	    		 if(m_faultIterator.hasNext())
	    			 m_faultIterators.add(m_faultIterator);
	    	 }
	    	 
	    	 JsonList list = new JsonList();
	    	 for(FaultIterator<JsonElement> m_faultIterator : m_faultIterators)
	    	 {
	    	   while(m_faultIterator.hasNext())
	    	   {
	    	     Set<? extends CorniTransformation> set = (Set<? extends CorniTransformation>) m_faultIterator.next();
	           
             for (CorniTransformation ct : set)
             {
               list.add(ct.toJson());
             }
	    	   }
	    	 }
	    	 list.isEmpty(); 
	    	 
	     } catch (JsonParseException | ParseException e) {
	       assert false; //Never supposed to happen....
	     }

	    // Create response
	    CallbackResponse cbr = null;
	 
	    // DEBUG: print state
	    //com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder();
	    //com.google.gson.Gson gson = builder.create();
	    //System.out.println(gson.toJson(m_interpreter));
	    return cbr;
	  }
}

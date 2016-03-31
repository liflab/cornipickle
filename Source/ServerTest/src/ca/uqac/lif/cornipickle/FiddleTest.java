package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.azrael.json.JsonSerializer;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.fiddle.Fiddle;
import ca.uqac.lif.cornipickle.fiddle.FiddlePair;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class FiddleTest
{
	Fiddle m_fiddle;
	
	static JsonParser s_parser = new JsonParser();
	
	JsonSerializer m_serializer = new JsonSerializer();
	
	@Before
	public void setUp()
	{
		m_fiddle = new Fiddle();
		m_serializer.addClassLoader(Interpreter.class.getClassLoader());
	}

	@Test
	public void testSetProperty()
	{
		String json = PackageFileReader.readPackageFile(this.getClass(), "data/fiddle/set-property-1.json");
		FiddlePair fp = m_fiddle.doOperation("", json);
		assertNotNull(fp);
		String state = fp.getState();
		assertFalse(state.isEmpty());
	}
	
	@Test
	public void testSimpleProperty() throws JsonParseException, SerializerException
	{
		String json;
		FiddlePair fp;
		Map<StatementMetadata,Verdict> verdicts;
		json = PackageFileReader.readPackageFile(this.getClass(), "data/fiddle/set-property-1.json");
		fp = m_fiddle.doOperation("", json);
		assertNotNull(fp);
		json = PackageFileReader.readPackageFile(this.getClass(), "data/fiddle/probe-1.json");
		fp = m_fiddle.doOperation(fp.getState(), json);
		assertNotNull(fp);
		verdicts = getVerdicts(fp);
		assertEquals(verdicts.size(), 1);
		for (StatementMetadata smd : verdicts.keySet())
		{
			Verdict v = verdicts.get(smd);
			assertEquals(v.getValue(), Verdict.Value.FALSE);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Map<StatementMetadata,Verdict> getVerdicts(FiddlePair fp) throws JsonParseException, SerializerException
	{
		String arg = fp.getArgument();
		JsonElement je = s_parser.parse(arg);
		Map<StatementMetadata,Verdict> verdicts = (Map<StatementMetadata,Verdict>) m_serializer.deserializeAs(je, HashMap.class);
		return verdicts;
	}

}

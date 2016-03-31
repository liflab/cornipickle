/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.azrael.json.JsonSerializer;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.serialization.CornipickleDeflateSerializer;
import ca.uqac.lif.cornipickle.serialization.CornipickleSerializer;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

public class SerializationTest
{
	protected CornipickleSerializer m_serializer;
	
	protected PackageFileReader m_reader;
	
	protected JsonParser m_jsonParser;

	@Before
	public void setUp() throws Exception
	{
		m_serializer = new CornipickleSerializer();
		m_reader = new PackageFileReader();
		m_jsonParser = new JsonParser();
	}

	@Test
	public void equalitySerializationTest1() throws SerializerException
	{
		JsonSerializer j_ser = new JsonSerializer();
		j_ser.addClassLoader(EqualsStatement.class.getClassLoader());
		EqualsStatement e1 = new EqualsStatement();
		e1.setLeft(new ElementPropertyPossessive("p", "width"));
		e1.setRight(new ElementPropertyPossessive("q", "height"));
		JsonElement e = j_ser.serialize(e1);
		assertNotNull(e);
		assertTrue("String too short to be meaningful", e.toString().length() > 10);
		EqualsStatement e2 = (EqualsStatement) j_ser.deserializeAs(e, EqualsStatement.class);
		assertNotNull(e2);
		assertNotNull(e2.m_left);
		assertNotNull(e2.m_right);
	}

	@Test
	public void equalitySerializationTest2() throws SerializerException
	{
		JsonSerializer j_ser = new JsonSerializer();
		j_ser.addClassLoader(AddOperation.class.getClassLoader());
		EqualsStatement e1 = new EqualsStatement();
		e1.setLeft(new ElementPropertyPossessive("p", "width"));
		AddOperation plus = new AddOperation(new ElementPropertyPossessive("p", "width"), new ElementPropertyPossessive("z", "color"));
		e1.setRight(plus);
		JsonElement e = j_ser.serialize(e1);
		assertNotNull(e);
		assertTrue("String too short to be meaningful", e.toString().length() > 10);
		EqualsStatement e2 = (EqualsStatement) j_ser.deserializeAs(e, EqualsStatement.class);
		assertNotNull(e2);
		assertNotNull(e2.m_left);
		assertNotNull(e2.m_right);
	}

	@Test
	public void emptyInterpreterTest() throws SerializerException
	{
		Interpreter i1 = new Interpreter();
		String e = m_serializer.serialize(i1);
		assertNotNull(e);
		Interpreter i2 = (Interpreter) m_serializer.deserializeAs(e, Interpreter.class);
		assertNotNull(i2);
	}

	@Test
	public void interpreterTest1() throws ParseException, SerializerException
	{
		Interpreter i1 = new Interpreter();
		i1.parseProperties("\"\"\"\n  @name Foo\n\"\"\"\nFor each $x in $(p) ($x's height is greater than 100).");
		String e = m_serializer.serialize(i1);
		assertNotNull(e);
		Interpreter i2 = (Interpreter) m_serializer.deserializeAs(e, Interpreter.class);
		assertNotNull(i2);
		Map<StatementMetadata,Statement> statements = i2.getStatements();
		assertNotNull(statements);
	}
	
	@Test
	public void interpreterTest2() throws ParseException, SerializerException, JsonParseException
	{
		Interpreter i1 = new Interpreter();
		i1.parseProperties("\"\"\"\n  @name Foo\n\"\"\"\nFor each $x in $(p) ($x's height is greater than 100).");
		String probe_contents = PackageFileReader.readPackageFile(this.getClass(), "data/probe-1.json");
		JsonElement je = m_jsonParser.parse(probe_contents);
		i1.evaluateAll(je);
		String e = m_serializer.serialize(i1);
		assertNotNull(e);
		Interpreter i2 = (Interpreter) m_serializer.deserializeAs(e, Interpreter.class);
		assertNotNull(i2);
		Map<StatementMetadata,Verdict> verdicts = i2.getVerdicts();
		assertEquals(verdicts.size(), 1);
		for (StatementMetadata smd : verdicts.keySet())
		{
			Verdict v = verdicts.get(smd);
			assertEquals(v.getValue(), Verdict.Value.FALSE);
		}
	}
	
	@Test
	public void interpreterTest3() throws ParseException, SerializerException, JsonParseException
	{
		Interpreter i1 = new Interpreter();
		i1.parseProperties("\"\"\"\n  @name Foo\n\"\"\"\nFor each $x in $(p) ($x's height is greater than 90).");
		String probe_contents = PackageFileReader.readPackageFile(this.getClass(), "data/probe-1.json");
		JsonElement je = m_jsonParser.parse(probe_contents);
		i1.evaluateAll(je);
		String e = m_serializer.serialize(i1);
		assertNotNull(e);
		Interpreter i2 = (Interpreter) m_serializer.deserializeAs(e, Interpreter.class);
		assertNotNull(i2);
		Map<StatementMetadata,Verdict> verdicts = i2.getVerdicts();
		assertEquals(verdicts.size(), 1);
		for (StatementMetadata smd : verdicts.keySet())
		{
			Verdict v = verdicts.get(smd);
			assertEquals(v.getValue(), Verdict.Value.TRUE);
		}
	}
	
	@Test
	public void interpreterTestDeflate3() throws ParseException, SerializerException, JsonParseException
	{
		CornipickleDeflateSerializer c_ser = new CornipickleDeflateSerializer();
		Interpreter i1 = new Interpreter();
		i1.parseProperties("\"\"\"\n  @name Foo\n\"\"\"\nFor each $x in $(p) ($x's height is greater than 90).");
		String probe_contents = PackageFileReader.readPackageFile(this.getClass(), "data/probe-1.json");
		JsonElement je = m_jsonParser.parse(probe_contents);
		i1.evaluateAll(je);
		String e = c_ser.serialize(i1);
		assertNotNull(e);
		Interpreter i2 = (Interpreter) c_ser.deserializeAs(e, Interpreter.class);
		assertNotNull(i2);
		Map<StatementMetadata,Verdict> verdicts = i2.getVerdicts();
		assertEquals(verdicts.size(), 1);
		for (StatementMetadata smd : verdicts.keySet())
		{
			Verdict v = verdicts.get(smd);
			assertEquals(v.getValue(), Verdict.Value.TRUE);
		}
	}

	@Test
	public void verdictTest1() throws ParseException, SerializerException
	{
		JsonSerializer j_ser = new JsonSerializer();
		Verdict v1 = new Verdict(Verdict.Value.TRUE);
		JsonElement e = j_ser.serialize(v1);
		assertNotNull(e);
		Verdict v2 = (Verdict) j_ser.deserializeAs(e, Verdict.class);
		assertNotNull(v2);
		assertEquals(v2.getValue(), Verdict.Value.TRUE);
	}
	
	@Test
	public void witnessTest1() throws ParseException, SerializerException
	{
		JsonSerializer j_ser = new JsonSerializer();
		Witness w1 = new Witness();
		JsonElement e = j_ser.serialize(w1);
		assertNotNull(e);
		Witness w2 = (Witness) j_ser.deserializeAs(e, Witness.class);
		assertNotNull(w2);
	}

}

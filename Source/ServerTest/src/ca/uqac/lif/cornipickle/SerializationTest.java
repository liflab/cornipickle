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
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.serialization.CornipickleSerializer;
import ca.uqac.lif.json.JsonElement;

public class SerializationTest
{
	protected final CornipickleSerializer m_serializer = new CornipickleSerializer(); 

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void equalitySerializationTest1() throws SerializerException
	{
		EqualsStatement e1 = new EqualsStatement();
		e1.setLeft(new ElementPropertyPossessive("p", "width"));
		e1.setRight(new ElementPropertyPossessive("q", "height"));
		JsonElement e = m_serializer.serialize(e1);
		assertNotNull(e);
		assertTrue("String too short to be meaningful", e.toString().length() > 10);
		EqualsStatement e2 = (EqualsStatement) m_serializer.deserializeAs(e, EqualsStatement.class);
		assertNotNull(e2);
		assertNotNull(e2.m_left);
		assertNotNull(e2.m_right);
	}

	@Test
	public void equalitySerializationTest2() throws SerializerException
	{
		EqualsStatement e1 = new EqualsStatement();
		e1.setLeft(new ElementPropertyPossessive("p", "width"));
		AddOperation plus = new AddOperation(new ElementPropertyPossessive("p", "width"), new ElementPropertyPossessive("z", "color"));
		e1.setRight(plus);
		JsonElement e = m_serializer.serialize(e1);
		assertNotNull(e);
		assertTrue("String too short to be meaningful", e.toString().length() > 10);
		EqualsStatement e2 = (EqualsStatement) m_serializer.deserializeAs(e, EqualsStatement.class);
		assertNotNull(e2);
		assertNotNull(e2.m_left);
		assertNotNull(e2.m_right);
	}

	@Test
	public void emptyInterpreterTest() throws SerializerException
	{
		Interpreter i1 = new Interpreter();
		JsonElement e = m_serializer.serialize(i1);
		assertNotNull(e);
		Interpreter i2 = (Interpreter) m_serializer.deserializeAs(e, Interpreter.class);
		assertNotNull(i2);
	}

	@Test
	public void interpreterTest1() throws ParseException, SerializerException
	{
		Interpreter i1 = new Interpreter();
		i1.parseProperties("\"\"\"\n  @name Foo\n\"\"\"\nFor each $x in $(p) ($x's height is greater than 100).");
		JsonElement e = m_serializer.serialize(i1);
		assertNotNull(e);
		Interpreter i2 = (Interpreter) m_serializer.deserializeAs(e, Interpreter.class);
		assertNotNull(i2);
		Map<StatementMetadata,Statement> statements = i2.getStatements();
		assertNotNull(statements);
	}

	@Test
	public void verdictTest1() throws ParseException, SerializerException
	{
		Verdict v1 = new Verdict();
		JsonElement e = m_serializer.serialize(v1);
		assertNotNull(e);
		Verdict v2 = (Verdict) m_serializer.deserializeAs(e, Verdict.class);
		assertNotNull(v2);
	}
	
	@Test
	public void witnessTest1() throws ParseException, SerializerException
	{
		Witness w1 = new Witness();
		JsonElement e = m_serializer.serialize(w1);
		assertNotNull(e);
		Witness w2 = (Witness) m_serializer.deserializeAs(e, Witness.class);
		assertNotNull(w2);
	}

}

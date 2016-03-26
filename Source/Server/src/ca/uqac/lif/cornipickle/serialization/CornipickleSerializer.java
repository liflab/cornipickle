/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2016 Sylvain Hallé

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
package ca.uqac.lif.cornipickle.serialization;

import ca.uqac.lif.azrael.Serializer;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.azrael.json.JsonSerializer;
import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

/**
 * Serializer for the Cornipickle interpreter. It calls a JSON serializer
 * in the background.
 * @author sylvain
 *
 */
public class CornipickleSerializer implements Serializer<String>
{
	protected transient JsonSerializer m_serializer;
	
	protected transient JsonParser m_parser;
	
	public CornipickleSerializer()
	{
		super();
		m_serializer = new JsonSerializer();
		m_parser = new JsonParser();
		// Already add class loaders
		addClassLoader(Interpreter.class.getClassLoader());
		addClassLoader(BnfParser.class.getClassLoader());
		
		addClassLoader(JsonElement.class.getClassLoader());
		addClassLoader(ca.uqac.lif.cornipickle.AddOperation.class.getClassLoader());
	}
	
	@Override
	public String serialize(Object o) throws SerializerException
	{
		return serializeAs(o, Interpreter.class);
	}
	
	@Override
	public String serializeAs(Object o, Class<?> clazz) throws SerializerException
	{
		JsonElement je = m_serializer.serialize(o);
		String je_string = je.toString();
		return je_string;
	}
	
	@Override
	public Interpreter deserializeAs(String e, Class<?> clazz) throws SerializerException
	{
		JsonElement je;
		try 
		{
			je = m_parser.parse(e);
		} 
		catch (JsonParseException e1)
		{
			throw new SerializerException(e1);
		}
		Interpreter i = (Interpreter) m_serializer.deserializeAs(je, Interpreter.class);
		return i;
	}

	@Override
	public void addClassLoader(ClassLoader cl)
	{
		m_serializer.addClassLoader(cl);
	}
}

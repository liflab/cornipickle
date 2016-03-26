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
package ca.uqac.lif.cornipickle;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.serialization.CornipickleDeflateSerializer;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;
import ca.uqac.lif.util.FileReadWrite;

public class Interpreter implements Originator<Interpreter,String>
{
	protected Map<StatementMetadata,Statement> m_statements;

	protected Map<String,SetDefinition> m_setDefs;

	protected CornipickleParser m_parser;

	protected Map<StatementMetadata,Verdict> m_verdicts;
	
	protected final transient CornipickleDeflateSerializer m_serializer;

	public Interpreter()
	{
		super();
		m_serializer = new CornipickleDeflateSerializer();
		clear();
	}

	public static void main(String[] args) throws IOException, JsonParseException, ParseException
	{
		// Read file contents
		String corni_filename = args[0];
		String json_filename = args[1];
		String corni_file_contents = FileReadWrite.readFile(corni_filename);
		String json_file_contents = FileReadWrite.readFile(json_filename);
		JsonElement jse = new JsonParser().parse(json_file_contents);

		Interpreter interpreter = new Interpreter();
		interpreter.parseProperties(corni_file_contents);
		interpreter.evaluateAll(jse);
		Map<StatementMetadata,Verdict> verdicts = interpreter.getVerdicts();
		System.out.println(verdicts);
	}

	public void resetHistory()
	{
		for (StatementMetadata key : m_statements.keySet())
		{
			Statement s = m_statements.get(key);
			s.resetHistory();
			m_verdicts.put(key, new Verdict(Verdict.Value.INCONCLUSIVE));
		}
	}

	/**
	 * Clears all data from this interpreter
	 */
	public void clear()
	{
		m_statements = new HashMap<StatementMetadata,Statement>();
		m_setDefs = new HashMap<String,SetDefinition>();
		m_parser = new CornipickleParser();
		m_verdicts = new HashMap<StatementMetadata,Verdict>();  	
	}

	public Set<String> getAttributes()
	{
		Set<String> out = new HashSet<String>();
		for (StatementMetadata m : m_statements.keySet())
		{
			Statement s = m_statements.get(m);
			out.addAll(AttributeExtractor.getAttributes(s));
		}
		return out;
	}

	public Set<String> getTagNames()
	{
		Set<String> out = new HashSet<String>();
		for (StatementMetadata m : m_statements.keySet())
		{
			Statement s = m_statements.get(m);
			out.addAll(TagNameExtractor.getTags(s));
		}
		return out;
	}

	public void parseProperties(String properties) throws ParseException
	{
		String[] lines = properties.split("\n");
		StatementMetadata meta = new StatementMetadata();
		boolean in_comment = false;
		StringBuilder le_string = new StringBuilder();
		String meta_param_name = "";
		StringBuilder meta_param_value = new StringBuilder();
		int i = 0;
		for (String line : lines)
		{
			line = line.trim();
			if (line.startsWith("#"))
			{
				// Do nothing
			}
			else if (line.startsWith("\"\"\""))
			{
				in_comment = !in_comment;
				if (in_comment)
				{
					meta_param_value = new StringBuilder();
				}
			}
			else if (in_comment)
			{
				if (line.startsWith("@"))
				{
					Pattern pat = Pattern.compile("@([^\\s]+)\\s(.*)");
					Matcher mat = pat.matcher(line);
					if (mat.find())
					{
						if (!meta_param_name.isEmpty())
						{
							meta.put(meta_param_name, meta_param_value.toString());
							meta_param_value = new StringBuilder();
						}
						meta_param_name = mat.group(1);
						meta_param_value.append(mat.group(2)).append(" ");
					}
				}
				else
				{
					meta_param_value.append(line.trim()).append(" ");
				}
			}
			else
			{
				le_string.append(line).append(" ");
				if (line.endsWith(".")) // End of language element: parse it
				{
					String property_string = le_string.toString().trim();
					property_string = property_string.substring(0, property_string.length() - 1); // Remove end period
					LanguageElement le = m_parser.parseLanguage(property_string);
					if (le instanceof PredicateDefinition)
					{
						// A user-defined predicate; we add it to the grammar
						PredicateDefinition pd = (PredicateDefinition) le;
						m_parser.addPredicateDefinition(pd);
						meta = new StatementMetadata();
						le_string = new StringBuilder();
					}
					else if (le instanceof Statement)
					{
						Statement s = (Statement) le;
						meta.put("uniqueid", Integer.toString(i));
						m_statements.put(meta, s);
						m_verdicts.put(meta, new Verdict(Verdict.Value.INCONCLUSIVE));
						meta = new StatementMetadata();
						le_string = new StringBuilder();
					}
					else if (le instanceof SetDefinitionExtension)
					{
						SetDefinitionExtension s = (SetDefinitionExtension) le;
						m_setDefs.put(s.getSetName(), s);
					}
					else if (le == null)
					{
						System.err.println("Error parsing the following statement\n" + property_string);
					}
					i++;
					meta = new StatementMetadata();
					le_string = new StringBuilder();
				}
			}
		}
	}

	public List<PredicateDefinition> getPredicates()
	{
		return m_parser.getPredicates();
	}

	public List<SetDefinition> getSetDefinitions()
	{
		List<SetDefinition> out = new LinkedList<SetDefinition>();
		for (String k : m_setDefs.keySet())
		{
			SetDefinition sd = m_setDefs.get(k);
			out.add(sd);
		}
		return out;
	}

	public Statement getProperty(StatementMetadata m)
	{
		return m_statements.get(m);
	}

	Map<StatementMetadata,Statement> getStatements()
	{
		return m_statements;
	}

	/**
	 * Object containing all the metadata associated to a
	 * Cornipickle statement. This metadata is generally declared
	 * through a "python-doc" comment block just before the
	 * statement's declaration. 
	 * @author sylvain
	 */
	public static class StatementMetadata extends HashMap<String,String>
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String toString()
		{
			StringBuilder out = new StringBuilder();
			for (String key : keySet())
			{
				out.append("@").append(key).append(" ");
				String value = get(key);
				out.append(value);
				out.append("\n");
			}
			return out.toString();
		}
	}

	public Map<StatementMetadata,Verdict> getVerdicts()
	{
		return m_verdicts;
	}

	public void evaluateAll(JsonElement j)
	{
		Map<StatementMetadata,Verdict> verdicts = new HashMap<StatementMetadata,Verdict>();
		Map<String,JsonElement> d = new HashMap<String,JsonElement>();
		// Fill dictionary with user-defined sets
		for (String set_name : m_setDefs.keySet())
		{
			SetDefinition def = m_setDefs.get(set_name);
			JsonList jl = new JsonList();
			jl.addAll(def.evaluate(null));
			d.put(set_name, jl);
		}
		for (StatementMetadata key : m_statements.keySet())
		{
			Statement s = m_statements.get(key);
			Verdict b = new Verdict(Verdict.Value.INCONCLUSIVE);
			if (s.isTemporal())
			{
				b = s.evaluate(j, d);
			}
			else
			{
				b = s.evaluateAtemporal(j, d);
			}
			verdicts.put(key, b);
		}
		m_verdicts = verdicts;
	}

	@Override
	public String saveToMemento()
	{
		String out = null;
		try
		{
			m_serializer.serialize(this);
		}
		catch (SerializerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public Interpreter restoreFromMemento(String memento)
	{
		Interpreter i = null;
		try
		{
			i = (Interpreter) m_serializer.deserializeAs(memento, Interpreter.class);
		}
		catch (SerializerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
}

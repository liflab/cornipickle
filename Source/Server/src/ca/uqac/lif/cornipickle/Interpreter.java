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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.util.FileReadWrite;

public class Interpreter
{
  
  Map<String,Statement> m_statements;

  Map<String,SetDefinition> m_setDefs;
  
  CornipickleParser m_parser;
  
  public Interpreter()
  {
    super();
    m_statements = new HashMap<String,Statement>();
    m_setDefs = new HashMap<String,SetDefinition>();
    m_parser = new CornipickleParser();
  }
  
  public static void main(String[] args) throws IOException, JsonParseException, ParseException
  {
    // Read file contents
    String corni_filename = args[0];
    String json_filename = args[1];
    String corni_file_contents = FileReadWrite.readFile(corni_filename);
    String json_file_contents = FileReadWrite.readFile(json_filename);
    JsonElement jse = JsonParser.parse(json_file_contents);
    
    Interpreter interpreter = new Interpreter();
    interpreter.parseProperties(corni_file_contents);
    Map<String,Boolean> verdicts = interpreter.evaluateAll(jse);
    System.out.println(verdicts);
  }
  
  public void parseProperties(String properties) throws ParseException
  {
    properties = sanitizeProperty(properties);
    // Split properties: dot followed by a new line
    String[] property_list = properties.split("\\.\n");
    int i = 0;
    for (String property : property_list)
    {
      LanguageElement le = m_parser.parseLanguage(property);
      if (le instanceof PredicateDefinition)
      {
        // A user-defined predicate; we add it to the grammar
        PredicateDefinition pd = (PredicateDefinition) le;
        m_parser.addPredicateDefinition(pd);
      }
      else if (le instanceof Statement)
      {
        Statement s = (Statement) le; 
        m_statements.put(Integer.toString(i), s);
        i++;
      }
      else if (le instanceof SetDefinitionExtension)
      {
        SetDefinitionExtension s = (SetDefinitionExtension) le;
        m_setDefs.put(s.getSetName(), s);
      }
      else if (le == null)
      {
        System.err.println("Error parsing the following statement\n" + property);
      }
    }
  }

  /**
   * Remove comments from property
   * @param property
   * @return
   */
  protected static String sanitizeProperty(String property)
  {
    // Remove Python-like comments (triple quotes)
    property = property.replaceAll("(?s)\"\"\".*?\"\"\"", "");
    property = property.trim();
    String[] lines = property.split("\n");
    StringBuilder out = new StringBuilder();
    for (String line : lines)
    {
      line = line.trim();
      if (!line.startsWith("#")) // Comment
      {
        out.append(line).append("\n");
      }
    }
    return out.toString();
  }

  public Map<String,Boolean> evaluateAll(JsonElement j)
  {
    Map<String,Boolean> verdicts = new HashMap<String,Boolean>();
    Map<String,JsonElement> d = new HashMap<String,JsonElement>();
    // Fill dictionary with user-defined sets
    for (String set_name : m_setDefs.keySet())
    {
      SetDefinition def = m_setDefs.get(set_name);
      JsonList jl = new JsonList();
      jl.addAll(def.evaluate(null));
      d.put(set_name, jl);
    }
    for (String key : m_statements.keySet())
    {
      Statement s = m_statements.get(key);
      boolean b = s.evaluate(j, d);
      verdicts.put(key, b);
    }
    return verdicts;
  }
}

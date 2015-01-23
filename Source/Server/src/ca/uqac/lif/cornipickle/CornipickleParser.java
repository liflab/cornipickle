package ca.uqac.lif.cornipickle;
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


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseNodeVisitor;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.util.EmptyException;

public class CornipickleParser implements ParseNodeVisitor 
{
  public static BnfParser s_parser = initializeParser();

  Map<String,Statement> m_statements;

  Map<String,SetDefinition> m_setDefs;

  protected Stack<LanguageElement> m_nodes;

  public CornipickleParser()
  {
    super();
    m_statements = new HashMap<String,Statement>();
    m_setDefs = new HashMap<String,SetDefinition>();
    reset();
  }

  public void reset()
  {
    m_nodes = new Stack<LanguageElement>();
  }

  protected static InputStream getGrammarStream()
  {
    return CornipickleParser.class.getResourceAsStream("cornipickle.bnf");
  }

  protected BnfParser getParser()
  {
    return s_parser;
  }

  /**
   * Initializes the BNF parser
   * @return
   */
  public static BnfParser initializeParser()
  {
    BnfParser parser = new BnfParser();
    String grammar = null;
    try
    {
      grammar = PackageFileReader.readPackageFile(getGrammarStream());
      parser.setGrammar(grammar);
    } 
    catch (IOException e)
    {
      e.printStackTrace();
    } 
    catch (InvalidGrammarException e)
    {
      e.printStackTrace();
    }
    return parser;
  }

  public void parseProperties(String properties) throws ParseException
  {
    properties = sanitizeProperty(properties);
    // Split properties: dot followed by a new line
    String[] property_list = properties.split("\\.\n");
    int i = 0;
    for (String property : property_list)
    {
      ParseNode node = null;
      try
      {
        node = s_parser.parse(property);
      }
      catch (BnfParser.ParseException e)
      {
        throw new ParseException("Error parsing properties");
      }
      if (node != null)
      {
        LanguageElement le = parseStatement(node);
        if (le instanceof Statement)
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

  public Statement parseStatement(String property) throws ParseException
  {
    LanguageElement el = parseLanguage(property);
    if (el == null || !(el instanceof Statement))
      return null;
    return (Statement) el;
  }

  public LanguageElement parseLanguage(String property) throws ParseException
  {
    ParseNode node = null;
    try
    {
      node = s_parser.parse(property);
    }
    catch (BnfParser.ParseException e)
    {
      throw new ParseException("Error parsing properties");
    }
    if (node != null)
    {
      return parseStatement(node);
    }
    return null;    
  }

  protected LanguageElement parseStatement(ParseNode root)
  {
    reset();
    root.postfixAccept(this);
    if (m_nodes.isEmpty())
    {
      return null;
    }
    return m_nodes.peek();
  }

  @Override
  public void visit(ParseNode node)
  {
    String node_token =  node.getToken();
    switch (node_token)
    {
    case "<binary_stmt>":
    case "<constant>":
    case "<css_attribute>":
    case "<el_or_not>":
    case "<number>":
    case "<property_or_const>":
    case "<S>":
    case "<statement>":
    case "<userdef_set>":
    case "<var_name>":
    {
      // Nothing to do: leave statement on stack
      break;
    }
    case "<css_selector>":
    {
      m_nodes.pop(); // )
      CssSelector out = (CssSelector) m_nodes.pop();
      m_nodes.pop(); // $(
      m_nodes.push(out);
      break;
    }
    case "<css_sel_contents>":
    {
      CssSelector sel = (CssSelector) m_nodes.pop();
      LanguageElement top = m_nodes.peek();
      if (top instanceof CssSelector)
      {
        // Merge both selectors
        CssSelector right = (CssSelector) m_nodes.pop();
        sel.mergeWith(right);
      }
      m_nodes.push(sel);
      break;
    }
    case "<css_sel_part>":
    {
      StringConstant part = (StringConstant) m_nodes.pop();
      CssSelector out = new CssSelector(part);
      m_nodes.push(out);
      break;
    }
    case "<def_set>":
    {
      ElementList set_elements = (ElementList) m_nodes.pop();
      m_nodes.pop(); // of
      m_nodes.pop(); // any
      m_nodes.pop(); // is
      StringConstant set_name = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // A
      SetDefinitionExtension out = new SetDefinitionExtension(set_name, set_elements);
      m_nodes.push(out);
      break;
    }
    case "<def_set_element>":
    {
      Property part = (Property) m_nodes.pop();
      ElementList out = new ElementList();
      out.add(part);
      m_nodes.push(out);
      break;        
    }
    case "<def_set_elements>":
    {
      ElementList sel = (ElementList) m_nodes.pop();
      while (true)
      {
        LanguageElement top = m_nodes.peek();
        if (top instanceof StringConstant)
        {
          StringConstant s = (StringConstant) top;
          if (s.toString().compareTo(",") == 0)
          {
            m_nodes.pop(); // ,
            // Merge both lists
            ElementList right = (ElementList) m_nodes.pop();
            sel.addAll(right);
          }
          else
          {
            break;
          }
        }
        else
        {
          break;
        }
      }
      m_nodes.push(sel);
      break;
    }
    case "<def_set_name>":
    {
      // Trim spaces
      LanguageElement el = m_nodes.pop();
      StringConstant sc = (StringConstant) el;
      String s = sc.toString();
      s = s.trim();
      m_nodes.push(new StringConstant(s));
      break;
    }
    case "<foreach>":
    {
      m_nodes.pop(); // )
      Statement statement = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      SetExpression set_name = (SetExpression) m_nodes.pop();
      m_nodes.pop(); // in
      StringConstant var_name = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // each
      m_nodes.pop(); // For
      ForAllStatement out = new ForAllStatement();
      out.setDomain(set_name);
      out.setInnerStatement(statement);
      out.setVariable(var_name);
      m_nodes.push(out);
      break;
    }
    case "<and>":
    {
      m_nodes.pop(); // (
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      m_nodes.pop(); // And
      m_nodes.pop(); // (
      Statement left = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      AndStatement out = new AndStatement();
      out.addOperand(left);
      out.addOperand(right);
      m_nodes.push(out);
      break;
    }
    case "<equality>":
    {
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // equals
      Property left = (Property) m_nodes.pop();
      EqualsStatement out = new EqualsStatement();
      out.setLeft(left);
      out.setRight(right);
      m_nodes.push(out);
      break;
    }
    case "<elem_property>":
    {
      StringConstant sel = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // 's
      StringConstant var = (StringConstant) m_nodes.pop();
      ElementProperty out = new ElementProperty(var, sel);
      m_nodes.push(out);
      break;
    }
    case "<gt>":
    {
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // than
      m_nodes.pop(); // greater
      m_nodes.pop(); // is
      Property left = (Property) m_nodes.pop();
      GreaterThanStatement out = new GreaterThanStatement();
      out.setLeft(left);
      out.setRight(right);
      m_nodes.push(out);
      break;
    }
    case "<set_name>":
    {
      LanguageElement el = m_nodes.pop();
      if (el instanceof CssSelector)
      {
        m_nodes.push(el);
      }
      else if (el instanceof StringConstant)
      {
        StringConstant set_name = (StringConstant) el;
        SetDefinition sd = new SetDefinition(set_name.toString());
        m_nodes.push(sd);
      }
      break;
    }
    case "<string>":
    {
      // Trim quotes
      LanguageElement el = m_nodes.pop();
      StringConstant sc = (StringConstant) el;
      String s = sc.toString();
      s = s.replaceAll("\"", "");
      m_nodes.push(new StringConstant(s));
      break;
    }
    default:
    {
      // This is a node that does not contain a label
      // Guess if this is a string or a number
      if (isNumeric(node_token))
      {
        NumberConstant out = new NumberConstant(node_token);
        m_nodes.push(out);
      }
      else
      {
        // A string
        StringConstant out = new StringConstant(node_token);
        m_nodes.push(out);
      }
    }
    }    
  }

  @Override
  public void pop()
  {
    // Nothing to do
  }

  protected static boolean isNumeric(String str)  
  {  
    try  
    {  
      Double.parseDouble(str);  
    }  
    catch(NumberFormatException nfe)  
    {  
      return false;  
    }  
    return true;  
  }

  public static class ParseException extends EmptyException
  {
    /**
     * Dummy UID
     */
    private static final long serialVersionUID = 1L;

    public ParseException(String message)
    {
      super(message);
    }
  }

}

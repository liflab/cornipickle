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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.bullwinkle.CaptureBlockParseNode;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseNodeVisitor;
import ca.uqac.lif.cornipickle.json.JsonNumber;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.util.EmptyException;

public class CornipickleParser implements ParseNodeVisitor 
{
  public BnfParser m_parser;
  
  protected Stack<LanguageElement> m_nodes;
  
  protected Map<String,PredicateDefinition> m_predicateDefinitions;
  
  protected int m_predicateCount;

  public CornipickleParser()
  {
    super();
    m_predicateCount = 0;
    m_parser = initializeParser();
    m_predicateDefinitions = new HashMap<String,PredicateDefinition>();
    reset();
  }
  
  public List<PredicateDefinition> getPredicates()
  {
  	List<PredicateDefinition> out = new LinkedList<PredicateDefinition>();
  	for (String k : m_predicateDefinitions.keySet())
  	{
  		PredicateDefinition pd = m_predicateDefinitions.get(k);
  		out.add(pd);
  	}
  	return out;
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
    return m_parser;
  }

  /**
   * Initializes the BNF parser
   * @return The initialized BNF parser
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
    //parser.setDebugMode(true);
    return parser;
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
      node = m_parser.parse(property);
    }
    catch (BnfParser.ParseException e)
    {
      throw new ParseException(e.toString());
    }
    if (node != null)
    {
      return parseStatement(node);
    }
    else
    {
      throw new ParseException("Error: the BNF parser returned null");
    }
    //return null;    
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
  
  public void addPredicateDefinition(PredicateDefinition pd)
  {
    // Add rules to the parser
    String rule_name = "USERDEFRULE" + m_predicateCount; // So that each predicate is unique
    pd.setRuleName(rule_name);
    BnfRule rule = pd.getRule();
    m_parser.addRule(rule);
    m_parser.addCaseToRule("<userdef_stmt>", "<" + rule_name + ">");
    // Add definition
    m_predicateDefinitions.put(rule_name, pd);
    m_predicateCount++;
  }

  @Override
  public void visit(ParseNode node)
  {
    String node_token =  node.getToken();
    if (node instanceof CaptureBlockParseNode)
    {
      CaptureBlock cb = new CaptureBlock(node_token);
      m_nodes.push(cb);
      return;
    }
    if (node_token.compareTo("<binary_stmt>") == 0) { }
    else if (node_token.compareTo("<unary_stmt>") == 0) { }
    else if (node_token.compareTo("<constant>") == 0) { }
    else if (node_token.compareTo("<css_attribute>") == 0) { }
    else if (node_token.compareTo("<el_or_not>") == 0) { }
    else if (node_token.compareTo("<elem_property>") == 0) { }
    else if (node_token.compareTo("<number>") == 0) { }
    else if (node_token.compareTo("<pred_pattern>") == 0) { }
    else if (node_token.compareTo("<property_or_const>") == 0) { }
    else if (node_token.compareTo("<S>") == 0) { }
    else if (node_token.compareTo("<statement>") == 0) { }
    else if (node_token.compareTo("<temporal_stmt>") == 0) { }
    else if (node_token.compareTo("<userdef_set>") == 0) { }
    else if (node_token.compareTo("<userdef_stmt>") == 0) { }
    else if (node_token.compareTo("<var_name>") == 0) { }
    else if (node_token.compareTo("<math>") == 0) { }
    else if (node_token.compareTo("<add>") == 0) 
    { 
      m_nodes.pop(); //(
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // plus
      Property left = (Property) m_nodes.pop();
      m_nodes.pop(); //)
      AddOperation out = new AddOperation(left, right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<and>") == 0)
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
    }
    else if (node_token.compareTo("<css_selector>") == 0)
    {
      m_nodes.pop(); // )
      CssSelector out = (CssSelector) m_nodes.pop();
      m_nodes.pop(); // $(
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<css_sel_contents>") == 0)
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
    }
    else if (node_token.compareTo("<css_sel_part>") == 0)
    {
      StringConstant part = (StringConstant) m_nodes.pop();
      CssSelector out = new CssSelector(part);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<def_set>") == 0)
    {
      ElementList set_elements = (ElementList) m_nodes.pop();
      m_nodes.pop(); // of
      m_nodes.pop(); // any
      m_nodes.pop(); // is
      StringConstant set_name = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // A
      SetDefinitionExtension out = new SetDefinitionExtension(set_name, set_elements);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<def_set_element>") == 0)
    {
      Property part = (Property) m_nodes.pop();
      ElementList out = new ElementList();
      out.add(part);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<def_set_elements>") == 0)
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
    }
    else if (node_token.compareTo("<def_set_name>") == 0)
    {
      // Trim spaces
      LanguageElement el = m_nodes.pop();
      StringConstant sc = (StringConstant) el;
      String s = sc.toString();
      s = s.trim();
      m_nodes.push(new StringConstant(s));
    }
    else if (node_token.compareTo("<div>") == 0) 
    { 
      m_nodes.pop(); //(
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // /
      Property left = (Property) m_nodes.pop();
      m_nodes.pop(); //)
      DivOperation out = new DivOperation(left, right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<eventually>") == 0)
    {
      m_nodes.pop(); // (
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      m_nodes.pop(); // Eventually
      Eventually out = new Eventually();
      out.setInnerStatement(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<exists>") == 0)
    {
      m_nodes.pop(); // )
      Statement statement = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      m_nodes.pop(); // that
      m_nodes.pop(); // such
      SetExpression set_name = (SetExpression) m_nodes.pop();
      m_nodes.pop(); // in
      StringConstant var_name = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // exists
      m_nodes.pop(); // There
      ExistsStatement out = new ExistsStatement();
      out.setDomain(set_name);
      out.setInnerStatement(statement);
      out.setVariable(var_name);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<foreach>") == 0)
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
    }
    else if (node_token.compareTo("<equality>") == 0)
    {
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // equals / is
      Property left = (Property) m_nodes.pop();
      EqualsStatement out = new EqualsStatement();
      out.setLeft(left);
      out.setRight(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<elem_property_pos>") == 0)
    {
      StringConstant sel = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // 's
      StringConstant var = (StringConstant) m_nodes.pop();
      ElementPropertyPossessive out = new ElementPropertyPossessive(var, sel);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<elem_property_com>") == 0)
    {
      StringConstant var = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // of
      StringConstant sel = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // the
      ElementPropertyComplement out = new ElementPropertyComplement(var, sel);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<globally>") == 0)
    {
      m_nodes.pop(); // (
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      m_nodes.pop(); // Globally
      Globally out = new Globally();
      out.setInnerStatement(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<gt>") == 0)
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
    }
    else if (node_token.compareTo("<implies>") == 0)
    {
    	m_nodes.pop(); // )
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      m_nodes.pop(); // Then
      m_nodes.pop(); // )
      Statement left = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      m_nodes.pop(); // If
      ImpliesStatement out = new ImpliesStatement();
      out.addOperand(left);
      out.addOperand(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<defined>") == 0)
    {
      m_nodes.pop(); //defined
      m_nodes.pop(); //is
      ElementProperty property = (ElementProperty) m_nodes.pop();
      IsDefinedStatement out = new IsDefinedStatement();
      out.setProperty(property);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<lt>") == 0)
    {
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // than
      m_nodes.pop(); // less
      m_nodes.pop(); // is
      Property left = (Property) m_nodes.pop();
      LessThanStatement out = new LessThanStatement();
      out.setLeft(left);
      out.setRight(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<let>") == 0)
    {
      m_nodes.pop(); // )
      Statement inner = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      Property prop = (Property) m_nodes.pop();
      m_nodes.pop(); // be
      StringConstant var = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // let
      LetStatement out = new LetStatement(var, prop);
      out.setStatement(inner);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<mult>") == 0) 
    { 
      m_nodes.pop(); //(
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // *
      Property left = (Property) m_nodes.pop();
      m_nodes.pop(); //)
      MultOperation out = new MultOperation(left, right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<negation>") == 0)
    {
      m_nodes.pop(); // (
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      m_nodes.pop(); // Not
      NegationStatement out = new NegationStatement();
      out.setInnerStatement(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<next>") == 0)
    {
      m_nodes.pop(); // (
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      m_nodes.pop(); // Next
      Next out = new Next();
      out.setInnerStatement(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<next_time>") == 0)
    {
      m_nodes.pop(); // )
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      m_nodes.pop(); // then
      m_nodes.pop(); // )
      Statement left = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      m_nodes.pop(); // time
      m_nodes.pop(); // next
      m_nodes.pop(); // The
      NextTime out = new NextTime();
      out.setLeft(left);
      out.setRight(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<never>") == 0)
    {
      m_nodes.pop(); // (
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      m_nodes.pop(); // Never
      Never out = new Never();
      out.setInnerStatement(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<or>") == 0)
    {
      m_nodes.pop(); // (
      Statement right = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      m_nodes.pop(); // Or
      m_nodes.pop(); // (
      Statement left = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      OrStatement out = new OrStatement();
      out.addOperand(left);
      out.addOperand(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<predicate>") == 0)
    {
      m_nodes.pop(); // )
      Statement statement = (Statement) m_nodes.pop();
      m_nodes.pop(); // (
      m_nodes.pop(); // when
      StringConstant pattern = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // that
      m_nodes.pop(); // say
      m_nodes.pop(); // We
      StringConstant rule_name = new StringConstant("TODO:userdef");
      PredicateDefinition out = new PredicateDefinition(rule_name);
      out.setPattern(pattern);
      out.setStatement(statement);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<regex_capture>") == 0)
    {
      StringConstant pattern = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // with
      ElementPropertyPossessive variable = (ElementPropertyPossessive) m_nodes.pop();
      m_nodes.pop(); // match
      RegexCapture out = new RegexCapture();
      out.setProperty(variable);
      out.setPattern(pattern.toString());
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<regex_match>") == 0)
    {
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // matches
      Property left = (Property) m_nodes.pop();
      RegexMatch out = new RegexMatch();
      out.setLeft(left);
      out.setRight(right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<set_name>") == 0)
    {
      LanguageElement el = m_nodes.pop();
      if (el instanceof CssSelector)
      {
        m_nodes.push(el);
      }
      else if (el instanceof RegexCapture)
      {
        m_nodes.push(el);
      }
      else if (el instanceof StringConstant)
      {
        StringConstant set_name = (StringConstant) el;
        SetDefinition sd = new SetDefinition(set_name.toString());
        m_nodes.push(sd);
      }
    }
    else if (node_token.compareTo("<string>") == 0)
    {
      // Trim quotes
      LanguageElement el = m_nodes.pop();
      StringConstant sc = (StringConstant) el;
      String s = sc.toString();
      s = s.replaceAll("\"", "");
      m_nodes.push(new StringConstant(s));
    }
    else if (node_token.compareTo("<sub>") == 0) 
    { 
      m_nodes.pop(); //(
      Property right = (Property) m_nodes.pop();
      m_nodes.pop(); // -
      Property left = (Property) m_nodes.pop();
      m_nodes.pop(); //)
      SubOperation out = new SubOperation(left, right);
      m_nodes.push(out);
    }
    else if (node_token.compareTo("<when>") == 0)
    {
      m_nodes.pop(); // (
      Statement in_statement = (Statement) m_nodes.pop();
      m_nodes.pop(); // )
      StringConstant var_after = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // now
      m_nodes.pop(); // is
      StringConstant var_before = (StringConstant) m_nodes.pop();
      m_nodes.pop(); // When
      WhenIsNow out = new WhenIsNow(var_before, var_after);
      out.setStatement(in_statement);
      m_nodes.push(out);
    }
    else
    {
      // This is a node that does not contain a label
      // Guess if this is a string or a number
      if (JsonNumber.isNumeric(node_token))
      {
        NumberConstant out = new NumberConstant(node_token);
        m_nodes.push(out);
      }
      else if (node_token.startsWith("<"))
      {
        // Most probably a user-defined non-terminal token
        StringConstant pattern = (StringConstant) m_nodes.pop(); // The pattern that was matched
        // Remove stack of any regex capture blocks associated to the match
        Vector<String> capture_blocks = new Vector<String>();
        while (!m_nodes.isEmpty() && m_nodes.peek() instanceof CaptureBlock)
        {
          CaptureBlock cb = (CaptureBlock) m_nodes.pop();
          capture_blocks.add(0, cb.toString());
        }
        String predicate_name = node_token;
        predicate_name = predicate_name.replaceAll("<", "");
        predicate_name = predicate_name.replaceAll(">", "");
        // In predicate call, put a reference to the predicate's definition
        PredicateDefinition pred_def = m_predicateDefinitions.get(predicate_name);
        if (pred_def == null)
        {
          System.err.println("Could not find definition for " + predicate_name);
          //throw new ParseException("Could not find definition for " + predicate_name);
        }
        PredicateCall out = new PredicateCall(pred_def, pattern.toString(), capture_blocks);
        m_nodes.push(out);
      }
      else
      {
        // A normal string
        StringConstant out = new StringConstant(node_token);
        m_nodes.push(out);
      }
    } 
  }

  @Override
  public void pop()
  {
    // Nothing to do
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

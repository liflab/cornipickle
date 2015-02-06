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
package ca.uqac.lif.cornipickle.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.NodePath;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.cornipickle.util.PackageFileReader;

public class JsonSlowParser extends JsonParser
{
  public static BnfParser s_parser = initializeParser();

  public static InputStream getGrammarStream()
  {
    return JsonSlowParser.class.getResourceAsStream("json.bnf");
  }

  /**
   * Initializes the BNF parser
   * @return The initialized Bullwinkle parser
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
    BnfParser.s_maxRecursionSteps = 500;
    return parser;
  }

  public JsonElement parse(String input) throws JsonParser.JsonParseException
  {
    ParseNode root = null;
    try
    {
      root = s_parser.parse(input);
    } 
    catch (ParseException e)
    {
      throw new JsonParseException(e.toString());
    }
    return parse(root);
  }

  protected static JsonElement parse(ParseNode root) throws JsonParseException
  {
    JsonElement out = null;
    if (root == null)
    {
      throw new JsonParseException("Top element is null");
    }
    String top_token = root.getToken();
    if (top_token.compareTo("<S>") != 0)
    {
      throw new JsonParseException("Top element is not <S>");
    }
    ParseNode object = root.getChildren().get(0);
    String object_token = object.getToken();
    if (object_token.compareTo("<object>") == 0)
    {
      JsonMap e = new JsonMap();
      ParseNode prop_list = object.getChildren().get(1);
      fillMap(e, prop_list);
      out = e;
    }
    else if (object_token.compareTo("<list>") == 0)
    {
      JsonList e = new JsonList();
      ParseNode list_contents = object.getChildren().get(1);
      fillList(e, list_contents);
      out = e;
    }
    else if (object_token.compareTo("<string>") == 0)
    {
      JsonString e = new JsonString(fillString(object));
      out = e;
    }
    else if (object_token.compareTo("<number>") == 0)
    {
      JsonNumber e = new JsonNumber(fillNumber(object));
      out = e;
    }
    return out;
  }

  protected static Number fillNumber(ParseNode root)
  {
    ParseNode child = root.getChildren().get(0);
    String s_value = child.getToken();
    return Float.parseFloat(s_value);
  }

  protected static String fillString(ParseNode root)
  {
    ParseNode child = root.getChildren().get(0);
    String contents = child.getToken();
    contents = contents.replace("\"", "");
    return contents;
  }  

  protected static void fillList(JsonList e, ParseNode list_contents) throws JsonParseException
  {
    List<ParseNode> list_contents_children = list_contents.getChildren();
    ParseNode list_first_el = list_contents_children.get(0);
    if (list_first_el.getToken().compareTo("") == 0)
    {
      // Empty token: list is over
      return;
    }
    JsonElement first_el = parse(list_first_el);
    e.add(first_el);
    if (list_contents_children.size() >= 3)
    {
      // Other elements
      ParseNode new_list_contents = list_contents_children.get(2);
      fillList(e, new_list_contents);
    }    
  }

  protected static void fillMap(JsonMap e, ParseNode prop_list) throws JsonParseException
  {
    List<ParseNode> prop_list_children = prop_list.getChildren();
    ParseNode prop_first_el = prop_list_children.get(0);
    if (prop_first_el.getToken().compareTo("ε") == 0)
    {
      // Empty token: property list is over
      return;
    }
    ParseNode attribute_node = NodePath.getPathFirst(prop_first_el, "<prop>.<attribute>.<string>.*");
    String attribute_name = attribute_node.getToken();
    attribute_name = attribute_name.replace("\"", "");
    ParseNode value_node = NodePath.getPathFirst(prop_first_el, "<prop>.<S>");
    JsonElement value = parse(value_node);
    e.put(attribute_name, value);
    if (prop_list_children.size() >= 3)
    {
      // Other elements
      ParseNode new_list_contents = prop_list_children.get(2);
      fillMap(e, new_list_contents);
    }    
  }
}

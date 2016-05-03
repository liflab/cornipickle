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

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CornipickleParserTest
{
  CornipickleParser parser;
  
  @Before
  public void setUp() throws Exception
  {
    parser = new CornipickleParser();
  }

  @Test
  public void testAddition1() throws ParseException
  {
    String line = "(200 + 100)";
    ParseNode pn = shouldParseAndNotNull(line, "<add>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof AddOperation))
    {
      fail("Got wrong type of object");
    }
  }

  @Test
  public void testAddition2() throws ParseException
  {
    String line1 = "(200 + 100)";
    ParseNode pn1 = shouldParseAndNotNull(line1, "<add>");
    LanguageElement e1 = parser.parseStatement(pn1);
    if (e1 == null)
    {
      fail("Parsing returned null");
    }
    if (!(e1 instanceof AddOperation))
    {
      fail("Got wrong type of object");
    }
    String line2 = "$d's width";
    ParseNode pn2 = shouldParseAndNotNull(line2, "<elem_property_pos>");
    LanguageElement e2 = parser.parseStatement(pn2);
    if (e2 == null)
    {
      fail("Parsing returned null");
    }
    if (!(e2 instanceof ElementPropertyPossessive))
    {
      fail("Got wrong type of object");
    }
    String line3 = line2 + " equals " + line1;
    ParseNode pn3 = shouldParseAndNotNull(line3, "<equality>");
    LanguageElement e3 = parser.parseStatement(pn3);
    if (e3 == null)
    {
      fail("Parsing returned null");
    }
    if (!(e3 instanceof EqualsStatement))
    {
      fail("Got wrong type of object");
    }
  }

  @Test
  public void testMult1() throws ParseException
  {
    String line = "(200 * 100)";
    ParseNode pn = shouldParseAndNotNull(line, "<mult>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof MultOperation))
    {
      fail("Got wrong type of object");
    }
  }

  @Test
  public void testDefined1() throws ParseException
  {
    String line = "$x's accesskey is defined";
    ParseNode pn = shouldParseAndNotNull(line, "<defined>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof IsDefinedStatement))
    {
      fail("Got wrong type of object");
    }
  }

  @Test
  public void testEquality1() throws ParseException
  {
    String line = "\"3\" equals \"3\"";
    ParseNode pn = shouldParseAndNotNull(line, "<equality>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof EqualsStatement))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testEquality2() throws ParseException
  {
    String line = "$x's height equals \"3\"";
    ParseNode pn = shouldParseAndNotNull(line, "<equality>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof EqualsStatement))
    {
      fail("Got wrong type of object");
    }
  }

  @Test
  public void testEquality3() throws ParseException
  {
    String line = "$x's height equals (250 + 50)";
    ParseNode pn = shouldParseAndNotNull(line, "<equality>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof EqualsStatement))
    {
      fail("Got wrong type of object");
    }
  }

  @Test
  public void testDisabled1() throws ParseException
  {
    String line = "$x's disabled matches \"true\"";
    ParseNode pn = shouldParseAndNotNull(line, "<regex_match>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof RegexMatch))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testLet1() throws ParseException
  {
    String line = "Let $arf be $x's height (\"3\" equals \"3\")";
    ParseNode pn = shouldParseAndNotNull(line, "<let>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof LetStatement))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testForEach1() throws ParseException
  {
    String line = "For each $x in $(p.menu) (\"3\" equals \"3\")";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof ForAllStatement))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testForEach2() throws ParseException
  {
    String line = "For each $x in $(p) ($x's width equals 100)";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof ForAllStatement))
    {
      fail("Got wrong type of object");
    }
  }

  @Test
  public void testExists() throws ParseException
  {
    String line = "There exists $d in $(#d) such that ( $d's width equals (200 + 100) )";
    ParseNode pn = shouldParseAndNotNull(line, "<exists>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof ExistsStatement))
    {
      fail("Got wrong type of object");
    }
  }
  
  /*
  @Test
  public void testForEach3() throws ParseException
  {
    String line = "For each $x in \"my set\" ($x's abc equals 1)";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof ForAllStatement))
    {
      fail("Got wrong type of object");
    }
  }*/

  @Test
  public void testCssSelector1() throws ParseException
  {
    String line = "$(p1.menu)";
    ParseNode pn = shouldParseAndNotNull(line, "<css_selector>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof CssSelector))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testCssSelector2() throws ParseException
  {
    String line = "$(p1.menu h1 h2#myid)";
    ParseNode pn = shouldParseAndNotNull(line, "<css_selector>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof CssSelector))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testUserDefinedSet2() throws ParseException
  {
    String line = "A tomato is any of \"abc\", \"def\", \"h1f\"";
    ParseNode pn = shouldParseAndNotNull(line, "<def_set>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof SetDefinition))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testUserDefinedSet1() throws ParseException
  {
    String line = "A tomato is any of \"abc\"";
    ParseNode pn = shouldParseAndNotNull(line, "<def_set>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof SetDefinition))
    {
      fail("Got wrong type of object");
    }
  }
  
  @Test
  public void testPredicate1() throws ParseException
  {
    String line = "We say that all is good when (1 equals 1)";
    ParseNode pn = shouldParseAndNotNull(line, "<predicate>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof PredicateDefinition))
    {
      fail("Got wrong type of object");
    }
    PredicateDefinition pd = (PredicateDefinition) e;
    BnfRule bnf_rule = pd.getRule();
    if (bnf_rule == null)
    {
      fail("BNF rule created from pattern is null");
    } 
  }
  
  @Test
  public void testPredicate2() throws ParseException
  {
    String line = "We say that $x is thin when ($x's width equals 0)";
    ParseNode pn = shouldParseAndNotNull(line, "<predicate>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof PredicateDefinition))
    {
      fail("Got wrong type of object");
    }
    PredicateDefinition pd = (PredicateDefinition) e;
    BnfRule bnf_rule = pd.getRule();
    if (bnf_rule == null)
    {
      fail("BNF rule created from pattern is null");
    }  
  }
  
  @Test
  public void testNextTime1() throws ParseException
  {
    String line = "The next time (0 equals 0) Then (0 equals 0)";
    ParseNode pn = shouldParseAndNotNull(line, "<next_time>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof NextTime))
    {
      fail("Got wrong type of object");
    }
  } 
  
  @Test
  public void testProperty1() throws ParseException
  {
    String line = "the height of $x";
    ParseNode pn = shouldParseAndNotNull(line, "<elem_property>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof ElementPropertyComplement))
    {
      fail("Got wrong type of object");
    }
  } 
  
  @Test
  public void testProperty2() throws ParseException
  {
    String line = "$x's height";
    ParseNode pn = shouldParseAndNotNull(line, "<elem_property>");
    LanguageElement e = parser.parseStatement(pn);
    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof ElementPropertyPossessive))
    {
      fail("Got wrong type of object");
    }
  } 
  


  @Test
  public void TestCornipickleParserGetPredicates(){

    PredicateDefinition pd1 = new PredicateDefinition(new StringConstant("pd1"));
    pd1.setPattern(new StringConstant("pattern1"));
    PredicateDefinition pd2 = new PredicateDefinition(new StringConstant("pd2"));
    pd2.setPattern(new StringConstant("pattern2"));
    PredicateDefinition pd3 = new PredicateDefinition(new StringConstant("pd3"));
    pd3.setPattern(new StringConstant("pattern3"));

    parser.addPredicateDefinition(pd1);
    parser.addPredicateDefinition(pd2);
    parser.addPredicateDefinition(pd3);

    LinkedList<PredicateDefinition> alpd = new LinkedList<PredicateDefinition>();
    alpd.add(pd1);
    alpd.add(pd2);
    alpd.add(pd3);

    List<PredicateDefinition> result = (LinkedList)parser.getPredicates();

    boolean ok = true;

    for (int i = 0 ; i<result.size();i++){
      if (!alpd.contains(result.get(i))){
        ok = false;
      }
    }

    assertTrue(ok);

  }















  @Test
  public void TestCornipickleParserAnd() throws ParseException{

    String line = "($y's right is greater than $x's left)\n" +
            "  And\n" +
            "  ($x's right is greater than $y's left)";

    ParseNode pn = shouldParseAndNotNull(line, "<and>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof AndStatement))
    {
      fail("Got wrong type of object");
    }

  }


  @Test
  public void TestCornipickleParserEventually() throws ParseException{

    String line = "Eventually ( \"3\" equals \"3\")\n";

    ParseNode pn = shouldParseAndNotNull(line, "<eventually>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof Eventually))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserDiv() throws ParseException{

    String line = "(3/3)\n";

    ParseNode pn = shouldParseAndNotNull(line, "<div>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof DivOperation))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserEventuallyWithin() throws ParseException{

    String line = "Eventually within 3 seconds ( \"3\" equals \"3\")\n";

    ParseNode pn = shouldParseAndNotNull(line, "<eventually_within>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof EventuallyWithin))
    {
      fail("Got wrong type of object");
    }

  }


  @Test
  public void TestCornipickleParserGlobally() throws ParseException{

    String line = "Always ( \"3\" equals \"3\")\n";

    ParseNode pn = shouldParseAndNotNull(line, "<globally>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof Globally))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserImplies() throws ParseException{

    String line = "If ( \"3\" equals \"3\") Then ( \"3\" equals \"3\")\n";

    ParseNode pn = shouldParseAndNotNull(line, "<implies>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof ImpliesStatement))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserLt() throws ParseException{

    String line = "\"3\" is less than \"3\"\n";

    ParseNode pn = shouldParseAndNotNull(line, "<lt>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof LessThanStatement))
    {
      fail("Got wrong type of object");
    }

  }


  @Test
  public void TestCornipickleParserNegation() throws ParseException{

    String line = "Not (\"3\" is less than \"3\")\n";

    ParseNode pn = shouldParseAndNotNull(line, "<negation>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof NegationStatement))
    {
      fail("Got wrong type of object");
    }

  }


  @Test
  public void TestCornipickleParserNext() throws ParseException{

    String line = "Next (\"3\" is less than \"3\")\n";

    ParseNode pn = shouldParseAndNotNull(line, "<next>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof Next))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserNever() throws ParseException{

    String line = "Never (\"3\" is less than \"3\")\n";

    ParseNode pn = shouldParseAndNotNull(line, "<never>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof Never))
    {
      fail("Got wrong type of object");
    }

  }

  @Test
  public void TestCornipickleParserOr() throws ParseException{

    String line = "($y's right is greater than $x's left) Or\n($x's right is greater than $y's left)";

    ParseNode pn = shouldParseAndNotNull(line, "<or>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof OrStatement))
    {
      fail("Got wrong type of object");
    }

  }


  @Test
  public void TestCornipickleParserRegexCapture() throws ParseException{

    String line = "match $x's width with \"[0-9]\"";

    ParseNode pn = shouldParseAndNotNull(line, "<regex_capture>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof RegexCapture))
    {
      fail("Got wrong type of object");
    }

  }





  @Test
  public void TestCornipickleParserSub() throws ParseException{

    String line = "(3-3)\n";

    ParseNode pn = shouldParseAndNotNull(line, "<sub>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof SubOperation))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserWhen() throws ParseException{

    String line = "When $d is now $y ( $d's width equals (200 + 100) )\n";

    ParseNode pn = shouldParseAndNotNull(line, "<when>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof WhenIsNow))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserSetNameRegex() throws ParseException{

    String line = "match $x's width with \"[0-9]\"";

    ParseNode pn = shouldParseAndNotNull(line, "<set_name>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof RegexCapture))
    {
      fail("Got wrong type of object");
    }

  }



  @Test
  public void TestCornipickleParserSetNameStringConstant() throws ParseException{

    String line = "\"[0-9]\"";

    ParseNode pn = shouldParseAndNotNull(line, "<userdef_set>");
    LanguageElement e = parser.parseStatement(pn);

    if (e == null)
    {
      fail("Parsing returned null");
    }
    if (!(e instanceof StringConstant))
    {
      fail("Got wrong type of object");
    }

  }










  public ParseNode shouldParseAndNotNull(String line, String start_symbol)
  {
    BnfParser p = parser.getParser();
    //p.setDebugMode(true);
    p.setStartRule(start_symbol);
    ParseNode pn = null;

    try
    {
      pn = p.parse(line);
    } catch (ParseException e)
    {
      fail(e.toString());
    }
    if (pn == null)
    {
      fail("Failed parsing expression through grammar: returned null");
    }
    return pn;
  }




}

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

import org.junit.Test;
import org.junit.Before;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.util.FileReadWrite;

@SuppressWarnings("unused")
public class GrammarTest
{
  CornipickleParser parser;
  
  @Before
  public void setUp() throws Exception
  {
    parser = new CornipickleParser();
  }
  
  @Test
  public void testRegexCapture() throws ParseException
  {
    String line = "match $x's text with \"yo man (.*)\"";
    ParseNode pn = shouldParseAndNotNull(line, "<regex_capture>", false);
  }  
  
  @Test
  public void testElementProperty1() throws ParseException
  {
    String line = "the height of $x";
    ParseNode pn = shouldParseAndNotNull(line, "<elem_property>", false);
  }
  
  @Test
  public void testElementProperty2() throws ParseException
  {
    String line = "$x's height";
    ParseNode pn = shouldParseAndNotNull(line, "<elem_property>", false);
  }
  
  @Test
  public void testUserDefinedSet2() throws ParseException
  {
    String line = "A tomato is any of \"abc\", \"def\", \"h1f\"";
    ParseNode pn = shouldParseAndNotNull(line, "<def_set>", false);
  }
  
  @Test
  public void testUserDefinedSet1() throws ParseException
  {
    String line = "A tomato is any of \"abc\"";
    ParseNode pn = shouldParseAndNotNull(line, "<def_set>", true);
  }
  
  @Test
  public void testEquality1() throws ParseException
  {
    String line = "\"3\" equals \"3\"";
    ParseNode pn = shouldParseAndNotNull(line, "<equality>", false);
  }
  
  @Test
  public void testEquality2() throws ParseException
  {
    String line = "$x's height equals \"3\"";
    ParseNode pn = shouldParseAndNotNull(line, "<equality>", false);
  }

  @Test
  public void testEquality3() throws ParseException
  {
    String line = "$b's disabled matches \"true\"";
    ParseNode pn = shouldParseAndNotNull(line, "<regex_match>", false);
  }
  
  @Test
  public void testLet1() throws ParseException
  {
    String line = "Let $arf be $x's height (\"3\" equals \"3\")";
    ParseNode pn = shouldParseAndNotNull(line, "<let>", false);
  }
  
  @Test
  public void testForEach1() throws ParseException
  {
    String line = "For each $x in $(p.menu) (\"3\" equals \"3\")";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>", false);
  }
  
  @Test
  public void testForEach2() throws ParseException
  {
    String line = "For each $x in $(p) ($x's width equals 100)";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>", false);
  }
  
  @Test
  public void testForEach3() throws ParseException
  {
    String line = "For each $x in match $y's text with \"yo man (.*?)\" ($x's value equals 100)";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>", false);
  }
  
  @Test
  public void testForEach4() throws ParseException
  {
    String line = "For each $u1 in match $x's text with \"Homepage for (.*)\" (\n      For each $u2 in match $g's text with \"Hello (.*)!\" (\n        $u1's value equals $u2's value\n      )\n    )";
    ParseNode pn = shouldParseAndNotNull(line, "<foreach>", false);
  } 

  @Test
  public void testExists1() throws ParseException
  {
    String line = "There exists $x in $(#d) such that ($x's width equals (100 plus 200) )";
    ParseNode pn = shouldParseAndNotNull(line, "<exists>", false);
  }
  
  @Test
  public void testIfThen1() throws ParseException
  {
    String line = "If (0 equals 0) Then (0 equals 0)";
    ParseNode pn = shouldParseAndNotNull(line, "<implies>", false);
  } 
  
  @Test
  public void testNextTime1() throws ParseException
  {
    String line = "The next time (0 equals 0) Then (0 equals 0)";
    ParseNode pn = shouldParseAndNotNull(line, "<next_time>", false);
  } 
  
  @Test
  public void testCssSelector1() throws ParseException
  {
    String line = "$(p1.menu)";
    ParseNode pn = shouldParseAndNotNull(line, "<css_selector>", false);
  }
  
  @Test
  public void testCssSelector2() throws ParseException
  {
    String line = "$(p1.menu h1 h2#myid)";
    ParseNode pn = shouldParseAndNotNull(line, "<css_selector>", false);
  }
  
  @Test
  public void testPredicate1() throws ParseException
  {
    String line = "We say that all is good when (1 equals 1)";
    ParseNode pn = shouldParseAndNotNull(line, "<predicate>", false);
  }
  
  @Test
  public void testPredicate2() throws ParseException
  {
    String line = "We say that $x is thin when ($x's width equals 0)";
    ParseNode pn = shouldParseAndNotNull(line, "<predicate>", false);
  }
  
  public ParseNode shouldParseAndNotNull(String line, String start_symbol, boolean debug_mode)
  {
    BnfParser p = parser.getParser();
    p.setStartRule(start_symbol);
    p.setDebugMode(debug_mode);
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
      fail("Failed parsing through grammar: returned null");
    }
    return pn;
  }

}

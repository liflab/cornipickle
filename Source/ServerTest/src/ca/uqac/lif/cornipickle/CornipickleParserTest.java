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
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;

public class CornipickleParserTest
{
  CornipickleParser parser;
  
  @Before
  public void setUp() throws Exception
  {
    parser = new CornipickleParser();
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
  
  public ParseNode shouldParseAndNotNull(String line, String start_symbol)
  {
    BnfParser p = parser.getParser();
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

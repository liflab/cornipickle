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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;
import ca.uqac.lif.cornipickle.util.PackageFileReader;

public class FullLoop
{
  CornipickleParser c_parser;
  JsonParser j_parser;
  
  @Before
  public void setUp() throws Exception
  {
    c_parser = new CornipickleParser();
    j_parser = new JsonParser();
  }

  @Test
  public void testForEach1() throws ParseException, JsonParseException, IOException
  {
    String property = "For each $x in $(p.maclasse) ($x's width equals 100)";
    Statement st = c_parser.parseStatement(property);
    if (st == null)
    {
      fail("Parsed statement is null");
    }
    JsonElement document = j_parser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/sample-4.json"));
    if (document == null)
    {
      fail("JSON document is null");
    }
    Verdict verdict = st.evaluate(document);
    if (!verdict.is(Verdict.Value.TRUE))
    {
      fail("Wrong verdict");
    }
  }
  
  @Test
  public void testForEach2() throws ParseException, JsonParseException, IOException
  {
    String property = "For each $x in $(p) ($x's width equals 100)";
    Statement st = c_parser.parseStatement(property);
    if (st == null)
    {
      fail("Parsed statement is null");
    }
    JsonElement document = j_parser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/sample-4.json"));
    if (document == null)
    {
      fail("JSON document is null");
    }
    Verdict verdict = st.evaluate(document);
    if (!verdict.is(Verdict.Value.FALSE))
    {
      fail("Wrong verdict");
    }
  }

}

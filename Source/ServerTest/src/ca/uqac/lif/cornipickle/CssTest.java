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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonPath;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.util.FileReadWrite;

@SuppressWarnings("unused")
public class CssTest
{

  CornipickleParser parser;
  
  JsonParser m_jsonParser;
  
  @Before
  public void setUp() throws Exception
  {
    parser = new CornipickleParser();
    m_jsonParser = new JsonParser();
  }

  @Test
  public void testCssFetch1() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("body h1", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetch2() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-4.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("h1 p", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 2)
    {
      fail("Expected size 2, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetch3() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-4.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch(".maclasse", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 2)
    {
      fail("Expected size 2, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetch4() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-9.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch(".square", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 3)
    {
      fail("Expected size 3, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetchArrow1() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("body>h1", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetchArrow2() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("body>CDATA", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 0)
    {
      fail("Expected size 0, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetchArrow3() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/witness-1.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("dummy>a", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 3)
    {
      fail("Expected size 3, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetchArrow4() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("body>h1#topmenu CDATA", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetchArrow5() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-10.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("div.topmenu>div.other>a", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 2)
    {
      fail("Expected size 2, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetchArrow6() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-11.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetch("div.topmenu>div.other>a", (JsonMap) jse, null);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
  }
  
  @Test
  public void testCssFetchArrow7() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json");
    JsonElement jse = m_jsonParser.parse(json);
    Map<String, JsonElement> d = new HashMap<String, JsonElement>();
    d.put("$x", m_jsonParser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/sample-12x.json")));
    List<JsonElement> list = CssSelector.fetch("$x", (JsonMap) jse, d);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
    if (!(JsonPath.getNumber(list.get(0),"cornipickleid").intValue() == 1))
    {
      fail("The wrong element is in the list");
    }
  }
  
  @Test
  public void testCssFetchArrow8() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json");
    JsonElement jse = m_jsonParser.parse(json);
    Map<String, JsonElement> d = new HashMap<String, JsonElement>();
    d.put("$x", m_jsonParser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/sample-12x.json")));
    List<JsonElement> list = CssSelector.fetch("$x>a", (JsonMap) jse, d);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
    if (!(JsonPath.getNumber(list.get(0),"cornipickleid").intValue() == 2))
    {
      fail("The wrong element is in the list");
    }
  }
  
  @Test
  public void testCssFetchArrow9() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-12.json");
    JsonElement jse = m_jsonParser.parse(json);
    Map<String, JsonElement> d = new HashMap<String, JsonElement>();
    d.put("$x", m_jsonParser.parse(PackageFileReader.readPackageFile(this.getClass(), "data/sample-12x.json")));
    List<JsonElement> list = CssSelector.fetch("window>$x>a", (JsonMap) jse, d);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 1)
    {
      fail("Expected size 1, got " + list.size());
    }
    if (!(JsonPath.getNumber(list.get(0),"cornipickleid").intValue() == 2))
    {
      fail("The wrong element is in the list");
    }
  }
  
  @Test
  public void testFetchAllChildrenRecursively() throws Exception
  {
    String json = PackageFileReader.readPackageFile(this.getClass(), "data/sample-4.json");
    JsonElement jse = m_jsonParser.parse(json);
    List<JsonElement> list = CssSelector.fetchAllChildrenRecursively(jse);
    if (list == null)
    {
      fail("Expected list, got null");
    }
    if (list.size() != 7)
    {
      fail("Expected size 7, got " + list.size());
    }
  }

  @Test
  public void testCssGetSelector(){

    CssSelector cs = new CssSelector("h");


    assertEquals(cs.getSelector().toString(), "h");

  }

  @Test
  public void TestCssSelectorToString(){

    CssSelector cs = new CssSelector("h");
    assertTrue("$(h)".equals(cs.toString()));

  }

}

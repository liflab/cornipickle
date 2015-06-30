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

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.json.JsonMap;
import ca.uqac.lif.cornipickle.json.JsonNumber;
import ca.uqac.lif.cornipickle.json.JsonString;

public class EvaluationTest
{

  @Before
  public void setUp() throws Exception
  {
  }

  @Test
  public void testEquality1()
  {
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(new NumberConstant(1));
    eq.setRight(new NumberConstant(1));
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    Verdict answer = eq.evaluate(null, d);
    if (!answer.is(Verdict.Value.TRUE))
    {
      fail("Expected true, got something else");
    }
  }
  
  @Test
  public void testEquality2()
  {
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(new NumberConstant(1));
    eq.setRight(new NumberConstant(1));
    AndStatement as = new AndStatement();
    as.addOperand(eq);
    NegationStatement ns = new NegationStatement();
    ns.setInnerStatement(eq);
    as.addOperand(ns);
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    Verdict answer = as.evaluate(null, d);
    if (!answer.is(Verdict.Value.FALSE))
    {
      fail("Expected false, got something else");
    }
  }

  
  
  @Test
  public void testProperty1()
  {
    ElementPropertyPossessive p = new ElementPropertyPossessive("$x", "height");
    JsonMap x = new JsonMap();
    x.put("height", new JsonNumber(100));
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(p);
    eq.setRight(new NumberConstant(100));
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", x);
    Verdict answer = eq.evaluate(null, d);
    if (!answer.is(Verdict.Value.TRUE))
    {
      fail("Expected true, got something else");
    }
  }
  
  @Test
  public void testProperty2()
  {
    ElementPropertyPossessive p = new ElementPropertyPossessive("$x", "height");
    JsonMap x = new JsonMap();
    x.put("width", new JsonNumber(100));
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(p);
    eq.setRight(new NumberConstant(100));
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", x);
    Verdict answer = eq.evaluate(null, d);
    if (!answer.is(Verdict.Value.FALSE))
    {
      fail("Expected false, got something else");
    }
  }

  @Test
  public void testProperty3()
  {
    ElementPropertyPossessive p = new ElementPropertyPossessive("$x", "height");
    JsonMap x = new JsonMap();
    x.put("height", new JsonNumber(100));
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(p);
    eq.setRight(new AddOperation(new NumberConstant(49), new NumberConstant(50)));
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", x);
    Verdict answer = eq.evaluate(null, d);
    if (!answer.is(Verdict.Value.FALSE))
    {
      fail("Expected false, got something else");
    }
  }
  
  @Test
  public void testForAll1()
  {
    // Create "document"
    JsonList el = new JsonList();
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(101));
      x.put("tagname", new JsonString("q"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      el.add(x);
    }
    JsonMap main = new JsonMap();
    main.put("children", el);
    main.put("tagname", new JsonString("h1"));
    
    // Create formula
    ElementPropertyPossessive prop = new ElementPropertyPossessive("$x", "width");
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(prop);
    eq.setRight(new NumberConstant(100));
    ForAllStatement foa = new ForAllStatement();
    foa.setVariable("$x");
    foa.setInnerStatement(eq);
    foa.setDomain(new CssSelector("p"));
    
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    Verdict answer = foa.evaluate(main, d);
    if (!answer.is(Verdict.Value.TRUE))
    {
      fail("Expected true, got something else");
    }
  }
  
  @Test
  public void testForAll2()
  {
    // Create "document"
    JsonList el = new JsonList();
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(101));
      x.put("tagname", new JsonString("q"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(222));
      x.put("tagname", new JsonString("p"));
      el.add(x);
    }
    JsonMap main = new JsonMap();
    main.put("children", el);
    main.put("tagname", new JsonString("h1"));
    
    // Create formula
    ElementPropertyPossessive prop = new ElementPropertyPossessive("$x", "width");
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(prop);
    eq.setRight(new NumberConstant(100));
    ForAllStatement foa = new ForAllStatement();
    foa.setVariable("$x");
    foa.setInnerStatement(eq);
    foa.setDomain(new CssSelector("p"));
    
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    Verdict answer = foa.evaluate(main, d);
    if (!answer.is(Verdict.Value.FALSE))
    {
      fail("Expected false, got something else");
    }
  }

  @Test
  public void testForAll3()
  {
    // Create "document"
    JsonList el = new JsonList();
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("d"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(101));
      x.put("tagname", new JsonString("p"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("d"));
      el.add(x);
    }
    JsonMap main = new JsonMap();
    main.put("children", el);
    main.put("tagname", new JsonString("div"));
    
    // Create formula
    ElementPropertyPossessive prop = new ElementPropertyPossessive("$x", "width");
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(prop);
    eq.setRight(new AddOperation(new NumberConstant(49), new NumberConstant(51)));
    ForAllStatement foa = new ForAllStatement();
    foa.setVariable("$x");
    foa.setInnerStatement(eq);
    foa.setDomain(new CssSelector("d"));
    
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    Verdict answer = foa.evaluate(main, d);
    if (!answer.is(Verdict.Value.TRUE))
    {
      fail("Expected true, got something else");
    }
  }
  
  @Test
  public void testNextTime1() throws ParseException
  {
    String expression = "The next time ($x's value equals 0) Then ($y's value equals 0)";
    CornipickleParser cp = new CornipickleParser();
    Statement st = cp.parseStatement(expression);
    Verdict answer;
    int event_nb = 0;
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", new JsonNumber(1));
    d.put("$y", new JsonNumber(1));
    answer = st.evaluate(null, d); event_nb++;
    if (!answer.is(Verdict.Value.INCONCLUSIVE))
    {
      fail("Wrong verdict at event " + event_nb);
    }
    d.put("$x", new JsonNumber(0));
    d.put("$y", new JsonNumber(1));
    answer = st.evaluate(null, d); event_nb++;
    if (!answer.is(Verdict.Value.FALSE))
    {
      fail("Wrong verdict at event " + event_nb);
    }
  }
  
  @Test
  public void testNextTime2() throws ParseException
  {
    String expression = "The next time ($x's value equals 0) Then ($y's value equals 0)";
    CornipickleParser cp = new CornipickleParser();
    Statement st = cp.parseStatement(expression);
    Verdict answer;
    int event_nb = 0;
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", new JsonNumber(1));
    d.put("$y", new JsonNumber(1));
    answer = st.evaluate(null, d); event_nb++;
    if (!answer.is(Verdict.Value.INCONCLUSIVE))
    {
      fail("Wrong verdict at event " + event_nb);
    }
    d.put("$x", new JsonNumber(1));
    d.put("$y", new JsonNumber(0));
    answer = st.evaluate(null, d); event_nb++;
    if (!answer.is(Verdict.Value.INCONCLUSIVE))
    {
      fail("Wrong verdict at event " + event_nb);
    }
  }
  
  @Test
  public void testNextTime3() throws ParseException
  {
    String expression = "The next time ($x's value equals 0) Then ($y's value equals 0)";
    CornipickleParser cp = new CornipickleParser();
    Statement st = cp.parseStatement(expression);
    Verdict answer;
    int event_nb = 0;
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", new JsonNumber(1));
    d.put("$y", new JsonNumber(1));
    answer = st.evaluate(null, d); event_nb++;
    if (!answer.is(Verdict.Value.INCONCLUSIVE))
    {
      fail("Wrong verdict at event " + event_nb);
    }
    d.put("$x", new JsonNumber(0));
    d.put("$y", new JsonNumber(0));
    answer = st.evaluate(null, d); event_nb++;
    if (!answer.is(Verdict.Value.TRUE))
    {
      fail("Wrong verdict at event " + event_nb);
    }
  }
  
  @Test
  public void testLet1() throws ParseException
  {
    // Create "document"
    JsonList el = new JsonList();
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(101));
      x.put("tagname", new JsonString("q"));
      el.add(x);
    }
    {
      JsonMap x = new JsonMap();
      x.put("width", new JsonNumber(100));
      x.put("tagname", new JsonString("p"));
      el.add(x);
    }
    JsonMap main = new JsonMap();
    main.put("children", el);
    main.put("tagname", new JsonString("h1"));
    
    // Create formula
    String expression = "For each $x in $(p) (Let $w be $x's width ($w's value is greater than 50))";
    CornipickleParser cp = new CornipickleParser();
    Statement st = cp.parseStatement(expression);
    Verdict answer;
    
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    answer = st.evaluate(main, d);
    if (!answer.is(Verdict.Value.TRUE))
    {
      fail("Expected true, got something else");
    }
  }

}

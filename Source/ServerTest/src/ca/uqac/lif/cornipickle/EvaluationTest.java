package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

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
    boolean answer = eq.evaluate(null, d);
    if (answer != true)
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
    boolean answer = as.evaluate(null, d);
    if (answer != false)
    {
      fail("Expected false, got something else");
    }
  }
  
  @Test
  public void testProperty1()
  {
    ElementProperty p = new ElementProperty("$x", "height");
    JsonMap x = new JsonMap();
    x.put("height", new JsonNumber(100));
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(p);
    eq.setRight(new NumberConstant(100));
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", x);
    boolean answer = eq.evaluate(null, d);
    if (answer != true)
    {
      fail("Expected true, got something else");
    }
  }
  
  @Test
  public void testProperty2()
  {
    ElementProperty p = new ElementProperty("$x", "height");
    JsonMap x = new JsonMap();
    x.put("width", new JsonNumber(100));
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(p);
    eq.setRight(new NumberConstant(100));
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    d.put("$x", x);
    boolean answer = eq.evaluate(null, d);
    if (answer != false)
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
    ElementProperty prop = new ElementProperty("$x", "width");
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(prop);
    eq.setRight(new NumberConstant(100));
    ForAllStatement foa = new ForAllStatement();
    foa.setVariable("$x");
    foa.setInnerStatement(eq);
    foa.setDomain(new CssSelector("p"));
    
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    boolean answer = foa.evaluate(main, d);
    if (answer != true)
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
    ElementProperty prop = new ElementProperty("$x", "width");
    EqualsStatement eq = new EqualsStatement();
    eq.setLeft(prop);
    eq.setRight(new NumberConstant(100));
    ForAllStatement foa = new ForAllStatement();
    foa.setVariable("$x");
    foa.setInnerStatement(eq);
    foa.setDomain(new CssSelector("p"));
    
    // Evaluate formula on document 
    HashMap<String,JsonElement> d = new HashMap<String,JsonElement>();
    boolean answer = foa.evaluate(main, d);
    if (answer != false)
    {
      fail("Expected false, got something else");
    }
  }

}

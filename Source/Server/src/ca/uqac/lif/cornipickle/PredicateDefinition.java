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

import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.bullwinkle.BnfRule.InvalidRuleException;
import ca.uqac.lif.json.JsonElement;

public class PredicateDefinition extends Statement
{
  /**
   * The non-terminal symbol associated to
   * that predicate definition in the interpreter's grammar
   */
  protected StringConstant m_ruleName;

  protected StringConstant m_pattern;

  /**
   * What variable name is associated to each capture group
   * in the regex pattern
   */
  protected Vector<String> m_captureGroups;

  /**
   * The BNF rule associated to this predicate in the interpreter's
   * grammar
   */
  protected BnfRule m_rule;

  /**
   * The definition of the predicate
   */
  protected Statement m_predicate;

  /**
   * Instantiates a predicate definition
   * @param ruleName The non-terminal symbol associated to
   *   that predicate definition in the interpreter's grammar
   */
  public PredicateDefinition(StringConstant ruleName)
  {
    super();
    m_ruleName = ruleName;
    m_captureGroups = new Vector<String>();
  }
  
  /**
   * Empty constructor, added only to simplify serialization
   */
  PredicateDefinition()
  {
	  super();
  }

  @Override
  public void resetHistory()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
    m_predicate.resetHistory();
  }

  public void setPattern(StringConstant pattern)
  {
    m_pattern = pattern;
    createBnfRule(pattern.toString()); 
  }

  public String getPattern()
  {
    return m_pattern.toString();
  }

  public Statement getPredicate()
  {
    return m_predicate;
  }

  /**
   * Turns the pattern into a BNF rule for the grammar
   * @param pattern The pattern
   * @return A regex rule with capture groups
   */
  protected BnfRule createBnfRule(String pattern)
  {
    BnfRule out = null;
    pattern = pattern.trim(); // Remove spurious space at the tail
    // Fetch the list of variables in the pattern
    Pattern pat = Pattern.compile("\\$[\\w\\d]+");
    Matcher mat = pat.matcher(pattern);
    while (mat.find())
    {
      String var_name = mat.group();
      m_captureGroups.add(var_name);
    }
    // Replace them by regex capture groups
    pattern = pattern.replaceAll("\\$[\\w\\d]+", "\\(\\\\\\$\\[\\\\w\\\\d\\]+\\)");
    // Create a BNF string and parse it
    StringBuilder rule = new StringBuilder();
    rule.append("<").append(m_ruleName).append("> := ");
    rule.append("^").append(pattern);
    try
    {
      out = BnfRule.parseRule(rule.toString());
    }
    catch (InvalidRuleException e)
    {
      // Should not happen
      e.printStackTrace();
    }
    // Set the BNF rule or null if parsing failed (should not)
    return out;
  }

  public String getCaptureGroup(int i)
  {
    return m_captureGroups.elementAt(i);
  }

  public void setStatement(Statement st)
  {
    m_predicate = st;
  }

  public String getRuleName()
  {
    return m_ruleName.toString();
  }

  public BnfRule getRule()
  {
    return createBnfRule(m_pattern.toString());
  }

  public void setRuleName(String name)
  {
    m_ruleName = new StringConstant(name);
  }

  public Statement getStatement()
  {
    return m_predicate;
  }

  @Override
  public String toString(String indent)
  {
    return m_pattern.toString();
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    m_verdict = m_predicate.evaluate(j, d);
    return m_verdict;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String,JsonElement> d)
  {
    return m_predicate.evaluateAtemporal(j, d);
  }

  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    m_predicate.prefixAccept(visitor);
    visitor.pop();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    m_predicate.postfixAccept(visitor);
    visitor.visit(this);
    visitor.pop();
  }

  @Override
  public PredicateDefinition getClone()
  {
    PredicateDefinition out = new PredicateDefinition(m_ruleName);
    out.m_captureGroups = m_captureGroups;
    out.m_predicate = m_predicate.getClone();
    return out;
  }

  @Override
  public boolean isTemporal()
  {
    return m_predicate.isTemporal();
  }
}

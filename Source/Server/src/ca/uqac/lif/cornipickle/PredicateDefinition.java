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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.bullwinkle.BnfRule.InvalidRuleException;
import ca.uqac.lif.cornipickle.json.JsonElement;

public class PredicateDefinition extends Statement
{
  protected StringConstant m_ruleName;
  
  protected StringConstant m_pattern;
  
  /**
   * What variable name is associated to each capture group
   * in the regex pattern
   */
  protected List<String> m_captureGroups;
  
  protected BnfRule m_rule;
  
  protected Statement m_predicate;
  
  public PredicateDefinition(StringConstant ruleName)
  {
    super();
    m_ruleName = ruleName;
    m_captureGroups = new LinkedList<String>();
  }
  
  public void setPattern(StringConstant pattern)
  {
    m_pattern = pattern;
    createBnfRule(pattern.toString()); 
  }
  
  /**
   * Turns the pattern into a BNF rule for the grammar
   * @param pattern The pattern
   * @return
   */
  protected void createBnfRule(String pattern)
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
    m_rule = out;
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
    return m_rule;
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
  public boolean evaluate(JsonElement j, Map<String, JsonElement> d)
  {
    // TODO Auto-generated method stub
    return false;
  }
}

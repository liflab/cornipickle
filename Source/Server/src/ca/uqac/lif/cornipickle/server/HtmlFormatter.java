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
package ca.uqac.lif.cornipickle.server;

import java.util.Stack;

import ca.uqac.lif.cornipickle.ComparisonStatement;
import ca.uqac.lif.cornipickle.CssSelector;
import ca.uqac.lif.cornipickle.ElementProperty;
import ca.uqac.lif.cornipickle.ExistsStatement;
import ca.uqac.lif.cornipickle.ForAllStatement;
import ca.uqac.lif.cornipickle.LanguageElement;
import ca.uqac.lif.cornipickle.LanguageElementVisitor;
import ca.uqac.lif.cornipickle.NAryStatement;
import ca.uqac.lif.cornipickle.NegationStatement;
import ca.uqac.lif.cornipickle.NumberConstant;
import ca.uqac.lif.cornipickle.PredicateCall;
import ca.uqac.lif.cornipickle.PredicateDefinition;
import ca.uqac.lif.cornipickle.SetDefinitionExtension;
import ca.uqac.lif.cornipickle.StringConstant;
import ca.uqac.lif.cornipickle.json.JsonElement;

public class HtmlFormatter implements LanguageElementVisitor
{
  protected Stack<StringBuilder> m_elements;
  
  public HtmlFormatter()
  {
    super();
    m_elements = new Stack<StringBuilder>();
  }
  
  public String getFormatted(LanguageElement root)
  {
    m_elements.clear();
    root.postfixAccept(this);
    if (m_elements.isEmpty())
    {
      return null;
    }
    StringBuilder top = m_elements.pop();
    return top.toString();
  }

  @Override
  public void visit(LanguageElement element)
  {
    if (element == null)
    {
      return;
    }
    StringBuilder out = new StringBuilder();
    if (element instanceof StringConstant)
    {
      StringConstant e = (StringConstant) element;
      out.append("<span class=\"string-constant\">").append(e.toString());
    }
    else if (element instanceof NumberConstant)
    {
      NumberConstant e = (NumberConstant) element;
      out.append("<span class=\"number-constant\">").append(e.toString());
    }
    else if (element instanceof NumberConstant)
    {
      NumberConstant e = (NumberConstant) element;
      out.append("<span class=\"number-constant\">").append(e.toString());
    }
    else if (element instanceof ElementProperty)
    {
      ElementProperty e = (ElementProperty) element;
      out.append("<span class=\"element-property\">");
      out.append("<span class=\"element-name\">").append(e.getElementName()).append("</span>");
      out.append("'s ");
      out.append("<span class=\"property-name\">").append(e.getPropertyName()).append("</span>");
    }
    else if (element instanceof ComparisonStatement)
    {
      ComparisonStatement e = (ComparisonStatement) element;
      out.append("<span class=\"comparison-statement\">");
      StringBuilder right = m_elements.pop(); // RHS
      StringBuilder left = m_elements.pop(); // LHS
      out.append(left).append(" ").append(e.getKeyword()).append(" ").append(right);
    }
    else if (element instanceof NAryStatement)
    {
      NAryStatement e = (NAryStatement) element;
      out.append("<span class=\"nary-statement\">");
      String keyword = e.getKeyword();
      for (int i = 0; i < e.getStatements().size(); i++)
      {
        if (i > 0)
        {
          out.append(" ").append(keyword).append(" ");
        }
        StringBuilder sts = m_elements.pop();
        out.append("(").append(sts).append(")");
      }      
    }
    else if (element instanceof ExistsStatement)
    {
      ExistsStatement e = (ExistsStatement) element;
      out.append("<span class=\"exists\">For each ");
      out.append("<span class=\"element-name\">").append(e.getVariable()).append("</span> in ");
      StringBuilder set_exp = m_elements.pop(); // Set expression
      out.append(set_exp);
      out.append(" (");
      StringBuilder inner_exp = m_elements.pop(); // Inner statement
      out.append(inner_exp);
      out.append(" )");
    }
    else if (element instanceof ForAllStatement)
    {
      ForAllStatement e = (ForAllStatement) element;
      out.append("<span class=\"forall\">For each ");
      out.append("<span class=\"element-name\">").append(e.getVariable()).append("</span> in ");
      StringBuilder set_exp = m_elements.pop(); // Set expression
      out.append(set_exp);
      out.append(" (");
      StringBuilder inner_exp = m_elements.pop(); // Inner statement
      out.append(inner_exp);
      out.append(" )");
    }
    else if (element instanceof PredicateCall)
    {
      PredicateCall e = (PredicateCall) element;
      out.append("<span class=\"predicate-call\">");
      out.append(e.getMatchedString());
      /*out.append("<span class=\"element-name\">").append(e.getVariable()).append("</span> in ");
      StringBuilder set_exp = m_elements.pop(); // Set expression
      out.append(set_exp);
      out.append(" (");
      StringBuilder inner_exp = m_elements.pop(); // Inner statement
      out.append(inner_exp);
      out.append(" )");*/
    }
    else if (element instanceof NegationStatement)
    {
      //NegationStatement e = (NegationStatement) element;
      out.append("<span class=\"negation\">");
      StringBuilder top = m_elements.pop();
      out.append("Not (").append(top).append(")");
    }
    else if (element instanceof PredicateDefinition)
    {
      PredicateDefinition e = (PredicateDefinition) element;
      out.append("<span class=\"predicate-definition\">");
      out.append("We say that ");
      out.append(e.getPattern());
      out.append(" when (");
      StringBuilder pred = m_elements.pop();
      out.append(pred);
      out.append(")");
    }
    else if (element instanceof SetDefinitionExtension)
    {
      SetDefinitionExtension e = (SetDefinitionExtension) element;
      out.append("<span class=\"set-definition-extension\">");
      out.append("A ");
      out.append("<span class=\"set-name\">");
      out.append(e.getName());
      out.append("</span>");
      out.append(" is any of ");
      boolean first = true;
      for (JsonElement el : e.getElements())
      {
        if (first)
        {
          first = false;
        }
        else
        {
          out.append(", ");
        }
        out.append(el.toString());
      }
      out.append(")");
    }
    else if (element instanceof CssSelector)
    {
      CssSelector e = (CssSelector) element;
      out.append("<span class=\"css-selector\">");
      out.append("$(");
      out.append(e.getSelector());
      out.append(")");
    }
    else
    {
      out.append("<span>");
    }
    m_elements.push(out);

  }

  @Override
  public void pop()
  {
    StringBuilder top = m_elements.pop();
    top.append("</span>");
    m_elements.push(top);
  }

}

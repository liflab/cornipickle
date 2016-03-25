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

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import ca.uqac.lif.json.JsonElement;

public abstract class SetExpression extends LanguageElement
{

  public SetExpression()
  {
    super();
  }
  
  public final List<JsonElement> evaluate(JsonElement j)
  {
    Map<String,JsonElement> d = new HashMap<String,JsonElement>();
    return evaluate(j, d);
  }
  
  public abstract List<JsonElement> evaluate(JsonElement t, Map<String, JsonElement> d);
  
  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    visitor.pop();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    visitor.pop();
  }
  
  public abstract SetExpression getClone();

}

package ca.uqac.lif.cornipickle.faultfinder.logic;

import ca.uqac.lif.cornipickle.Property;
import ca.uqac.lif.cornipickle.faultfinder.Expression;
import ca.uqac.lif.json.JsonElement;

public interface CorniExpressionVisitor {
  
  public void visit(Expression<JsonElement> expression);
  
  public void visit(Property e);
}

package ca.uqac.lif.cornipickle.faultfinder.logic;

public interface LogicalExpressionVisitor<T> 
{
	public void visit(LogicalExpression<T> expression);
	
	public void visit(Evaluable e);
}

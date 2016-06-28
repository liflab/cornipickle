package ca.uqac.lif.cornipickle;

import java.util.Map;

import ca.uqac.lif.json.JsonElement;

public class Context extends Statement {
  protected Statement m_innerStatement;
  
  public Context()
  {
    super();
  }
  
  public void setInnerStatement(Statement s)
  {
    m_innerStatement = s;
  }
  
  @Override
  public String toString(String indent) {
    StringBuilder out = new StringBuilder();
    out.append(indent).append("The following rules apply when (\n");
    out.append(m_innerStatement.toString(indent + "  "));
    out.append("\n").append(indent).append(")");
    return out.toString();
  }

  @Override
  public void prefixAccept(LanguageElementVisitor visitor) {
    visitor.visit(this);
    m_innerStatement.prefixAccept(visitor);
    visitor.pop();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor) {
    m_innerStatement.postfixAccept(visitor);
    visitor.visit(this);
    visitor.pop();
  }

  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d) {
    return m_innerStatement.evaluateAtemporal(j, d);
  }

  @Override
  public boolean isTemporal() {
    return false;
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d) {
    return m_innerStatement.evaluateTemporal(j, d);
  }

  @Override
  public Statement getClone() {
    Context out = new Context();
    out.setInnerStatement(m_innerStatement.getClone());
    return out;
  }

  @Override
  public void resetHistory() {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
  }

}

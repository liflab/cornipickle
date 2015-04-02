package ca.uqac.lif.cornipickle;

import ca.uqac.lif.cornipickle.json.JsonElement;

public class Verdict
{
  public static enum Value {TRUE, FALSE, INCONCLUSIVE};
  
  protected Value m_value;
  
  protected Witness m_witnessTrue;
  
  protected Witness m_witnessFalse;
  
  public Verdict()
  {
    this(Value.INCONCLUSIVE);
  }
  
  public Verdict(Value v)
  {
    super();
    m_witnessTrue = new Witness();
    m_witnessFalse = new Witness();
    m_value = v;
  }
  
  public boolean is(Value v)
  {
    return v == m_value;
  }
  
  public void setValue(Value v)
  {
    m_value = v;
  }
  
  public Verdict.Value getValue()
  {
    return m_value;
  }
  
  /**
   * Computes the three-valued disjunction (OR) of the current
   * verdict and the one passed as an argument
   * @param x The other verdict
   */
  public void disjoin(Verdict x)
  {
    disjoin(x, null);
  }
  
  /**
   * Computes the three-valued disjunction (OR) of the current
   * verdict and the one passed as an argument
   * @param x The other verdict
   * @param e The element to add as an explanation to the verdict
   */
  public void disjoin(Verdict x, JsonElement e)
  {
    if (x.is(Value.TRUE))
    {
      m_value = Value.TRUE;
      if (!x.m_witnessTrue.isEmpty())
      {
        Witness w = new Witness(e);
        w.add(x.m_witnessTrue);
        m_witnessTrue.add(w);
      }
    }
    else if (!is(Value.TRUE))
    {
      if (x.is(Value.INCONCLUSIVE))
      {
        m_value = Value.INCONCLUSIVE;
      }
      //if (!x.m_witnessFalse.isEmpty())
      {
        Witness w = new Witness(e);
        w.add(x.m_witnessFalse);
        m_witnessFalse.add(w);
      }
    }
  }
  
  /**
   * Computes the three-valued conjunction (AND) of the current
   * verdict and the one passed as an argument
   * @param x The other verdict
   * @param e The element to add as an explanation to the verdict
   */
  public void conjoin(Verdict x, JsonElement e)
  {
    if (x.is(Value.FALSE))
    {
      m_value = Value.FALSE;
      if (!x.m_witnessFalse.isEmpty())
      {
        Witness w = new Witness(e);
        w.add(x.m_witnessFalse);
        m_witnessFalse.add(w);
      }
    }
    else if (!is(Value.FALSE))
    {
      if (x.is(Value.INCONCLUSIVE))
      {
        m_value = Value.INCONCLUSIVE;        
      }
      //if (!x.m_witnessTrue.isEmpty())
      {
        Witness w = new Witness(e);
        w.add(x.m_witnessTrue);
        m_witnessTrue.add(w);
      }
    }
  }
  
  /**
   * Computes the three-valued conjunction (AND) of the current
   * verdict and the one passed as an argument
   * @param x The other verdict
   */
  public void conjoin(Verdict x)
  {
    conjoin(x, null);
  }
  
  /**
   * Computes the three-valued negation (NOT) of the
   * verdict passed as an argument
   * @param x The other verdict
   */
  public void negate(Verdict x)
  {
    negate(x, null);
  }
  
  /**
   * Computes the three-valued negation (NOT) of the
   * verdict passed as an argument
   * @param x The other verdict
   * @param e The element to add as an explanation to the verdict
   */
  public void negate(Verdict x, JsonElement e)
  {
    if (x.is(Value.TRUE))
    {
      m_value = Value.FALSE;
    }
    else if (x.is(Value.FALSE))
    {
      m_value = Value.TRUE;
    }
    if (!x.m_witnessTrue.isEmpty())
    {
      Witness w = new Witness(e);
      w.add(x.m_witnessTrue);
      m_witnessFalse.add(w);
    }
    if (!x.m_witnessFalse.isEmpty())
    {
      Witness w = new Witness(e);
      w.add(x.m_witnessFalse);
      m_witnessTrue.add(w);
    }
  }
  
  public void setWitnessTrue(Witness w)
  {
    m_witnessTrue = w;
  }
  
  public void setWitnessFalse(Witness w)
  {
    m_witnessFalse = w;
  }
  
  public Witness getWitnessFalse()
  {
    return m_witnessFalse;
  }
  
  public Witness getWitnessTrue()
  {
    return m_witnessTrue;
  }
  
  public Witness getWitness()
  {
    if (m_value == Value.FALSE)
    {
      return m_witnessFalse;
    }
    else if (m_value == Value.TRUE)
    {
      return m_witnessTrue;
    }
    return null;
  }
  
  @Override
  public String toString()
  {
    StringBuilder out = new StringBuilder();
    if (m_value == Value.FALSE)
    {
      out.append("F ").append(m_witnessFalse);
    }
    else if (m_value == Value.TRUE)
    {
      out.append("T ").append(m_witnessTrue);
    }
    else
    {
      out.append("?");
    }
    return out.toString();
  }
}

package ca.uqac.lif.cornipickle;

public class Verdict
{
  public static enum Value {TRUE, FALSE, INCONCLUSIVE};
  
  protected Value m_value;
  
  protected Witness m_witnessTrue;
  
  protected Witness m_witnessFalse;
  
  protected Value m_polarity;
  
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
  
  public void setPolarity(Value v)
  {
    m_polarity = v;
  }
  
  public Value getPolarity()
  {
    return m_polarity;
  }
  
  public boolean is(Value v)
  {
    return v == m_value;
  }
  
  public void setValue(Value v)
  {
    m_value = v;
  }
  
  /**
   * Computes the three-valued disjunction (OR) of the current
   * verdict and the one passed as an argument
   * @param x The other verdict
   * @return The three-valued disjunction
   */
  public void disjoin(Verdict x)
  {
    if (x.is(Value.TRUE))
    {
      m_value = Value.TRUE;
    }
    else if (!is(Value.TRUE) && x.is(Value.INCONCLUSIVE))
    {
      m_value = Value.INCONCLUSIVE;
    }
    m_witnessTrue.add(x.m_witnessTrue);
    m_witnessFalse.add(x.m_witnessFalse);
  }
  
  /**
   * Computes the three-valued conjunction (AND) of the current
   * verdict and the one passed as an argument
   * @param x The other verdict
   * @return The three-valued disjunction
   */
  public void conjoin(Verdict x)
  {
    if (x.is(Value.FALSE))
    {
      m_value = Value.FALSE;
    }
    else if (!is(Value.FALSE) && x.is(Value.INCONCLUSIVE))
    {
      m_value = Value.INCONCLUSIVE;
    }
    m_witnessTrue.add(x.m_witnessTrue);
    m_witnessFalse.add(x.m_witnessFalse);
  }
  
  /**
   * Computes the three-valued negation (NOT) of the
   * verdict passed as an argument
   * @param x The other verdict
   * @return The three-valued negation
   */
  public void negate(Verdict x)
  {
    if (x.is(Value.TRUE))
    {
      m_value = Value.FALSE;
    }
    else if (x.is(Value.FALSE))
    {
      m_value = Value.TRUE;
    }
    m_witnessTrue.add(x.m_witnessFalse);
    m_witnessFalse.add(x.m_witnessTrue);
  }
  
  public void setWitnessTrue(Witness w)
  {
    m_witnessTrue = w;
  }
  
  public void setWitnessFalse(Witness w)
  {
    m_witnessFalse = w;
  }
}

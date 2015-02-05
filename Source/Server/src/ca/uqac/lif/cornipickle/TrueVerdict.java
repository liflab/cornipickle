package ca.uqac.lif.cornipickle;

public class TrueVerdict
{
  public static TrueVerdict getTrueVerdict()
  {
    if (ref == null)
        // it's ok, we can call this constructor
        ref = new TrueVerdict();        
    return ref;
  }

  public Object clone()
    throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException(); 
    // that'll teach 'em
  }

  private static TrueVerdict ref;
}

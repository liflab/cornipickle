package ca.uqac.lif.cornipickle;

import java.util.Set;

import ca.uqac.lif.cornipickle.transformations.CorniTransformation;

public interface HasTransformations
{
  /**
   * Returns the transformations found during evaluation
   * @return a list of transformations
   */
  public Set<CorniTransformation> getTransformations();
  
  /**
   * Returns the list of transformation before emptying it.
   * @return a list of transformations
   */
  public Set<CorniTransformation> flushTransformations();
}

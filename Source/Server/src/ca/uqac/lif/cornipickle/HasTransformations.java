package ca.uqac.lif.cornipickle;

import java.util.List;

import ca.uqac.lif.cornipickle.transformations.CorniTransformation;

public interface HasTransformations
{
  /**
   * Returns the transformations found during evaluation
   * @return a list of transformations
   */
  public List<CorniTransformation> getTransformations();
  
  /**
   * Returns the list of transformation before emptying it.
   * @return a list of transformations
   */
  public List<CorniTransformation> flushTransformations();
}

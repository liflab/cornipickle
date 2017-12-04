package ca.uqac.lif.cornipickle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.bullwinkle.ParseNode;

public class OperationTest
{
  CornipickleParser m_parser;
  
  @Before
  public void setUp() throws Exception
  {
    m_parser = new CornipickleParser();
  }
  
  @Test
  public void isConstantOperationTest()
  {
    Operation operation = new AddOperation(new NumberConstant(3), new NumberConstant(3));
    assertTrue(operation.isConstantOperation());
    
    operation = new AddOperation(new ElementPropertyPossessive("$x", "bottom"), new NumberConstant(5));
    assertFalse(operation.isConstantOperation());
    
    operation = new AddOperation(new SubOperation(new NumberConstant(2), new NumberConstant(1)), new NumberConstant(5));
    assertTrue(operation.isConstantOperation());
    
    operation = new AddOperation(new SubOperation(new ElementPropertyPossessive("$x", "bottom"), new NumberConstant(1)), 
        new NumberConstant(5));
    assertFalse(operation.isConstantOperation());
  }
}

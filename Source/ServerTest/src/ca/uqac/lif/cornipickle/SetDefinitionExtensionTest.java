package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SetDefinitionExtensionTest {
	
	SetDefinitionExtension se;
	@Before
	public void setUp()throws Exception{
		se =new SetDefinitionExtension("Cornipickle");
	}
	@Test
	public void testGetElements() {
		assertTrue(se.getElements().toString().equals("[]"));
	}

	/*@Test
	public void testAddElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testToJsonElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSetName() {
		fail("Not yet implemented");
	}*/

}

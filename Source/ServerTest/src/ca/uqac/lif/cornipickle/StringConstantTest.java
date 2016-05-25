package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.json.JsonString;

public class StringConstantTest {
	
	@Test
	public void testStringConstantJsonString() {
		JsonString je = new JsonString("Cornipickle");
		StringConstant sc=new StringConstant(je);
		assertTrue(sc.m_value.stringValue().equals("Cornipickle"));
	}

}

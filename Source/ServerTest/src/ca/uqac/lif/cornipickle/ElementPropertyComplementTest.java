package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElementPropertyComplementTest {

	@Test
	public void testToStringString() {
		String s1 ="cornipickle1";
		String s2 ="cornipickle2";
		ElementPropertyComplement epc = new ElementPropertyComplement(s1,s2);
		assertTrue(epc.toString().equals("the cornipickle2 of cornipickle1"));
	}

	@Test
	public void testElementPropertyComplement() {
		ElementPropertyComplement epc = new ElementPropertyComplement();
		assertTrue(epc.m_elementName==null && epc.m_propertyName==null);
	}

	@Test
	public void testElementPropertyComplementStringString() {
		String s1 ="cornipickle1";
		String s2 ="cornipickle2";
		ElementPropertyComplement epc = new ElementPropertyComplement(s1,s2);
		assertTrue(epc.m_elementName.toString().equals("cornipickle1") && epc.m_propertyName.toString().equals("cornipickle2"));
	}

	@Test
	public void testGetClone() {
		String s1 ="cornipickle1";
		String s2 ="cornipickle2";
		ElementPropertyComplement epc = new ElementPropertyComplement(s1,s2);
		ElementPropertyComplement epc2 = epc.getClone();
		assertTrue(epc2.toString().equals(epc.toString()));
	}

}

package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Test;

public class TemporalStatementTest {

	@Test
	public void testIsTemporal() {
		TemporalStatement ts=new NextTime();
		assertTrue(ts.isTemporal());
	}

}

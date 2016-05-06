package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class TemporalStatementTest {

	@Test
	public void testIsTemporal() {
		TemporalStatement ts=new NextTime();
		assertTrue(ts.isTemporal());
	}

}

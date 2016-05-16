package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import ca.uqac.lif.cornipickle.Verdict.Value;

public class TrueVerdictTest {

	@Test
	public void testGetTrueVerdict() {
		TrueVerdict tv= new TrueVerdict();
		System.out.println(tv.getTrueVerdict().toString());
		Pattern pattern=Pattern.compile("ca.uqac.lif.cornipickle.");
		Matcher matcher =pattern.matcher(tv.getTrueVerdict().toString());
		if (matcher.find()){
			assertTrue(true);
		}else{
			assertFalse(false);
		}	
	}
}

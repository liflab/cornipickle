package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;

public class SetExpressionTest {

	@Test
	public void testPostfixAccept() {
		SetExpression se = new SetDefinition();
		LanguageElementVisitor test = new AttributeExtractor();
		se.postfixAccept(test);
		assertTrue(true);

	}


}

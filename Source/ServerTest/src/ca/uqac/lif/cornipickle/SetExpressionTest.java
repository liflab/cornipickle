package ca.uqac.lif.cornipickle;

import static org.junit.Assert.*;

import org.junit.Test;

public class SetExpressionTest {

	@Test
	public void testPostfixAccept() {
		SetExpression se = new SetDefinition();
		LanguageElementVisitor test = new AttributeExtractor();
		se.postfixAccept(test);
		assertTrue(true);

	}


}

package ca.uqac.lif.cornipickle;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by paull on 17/05/2016.
 */
public class RegexCaptureTest {

    RegexCapture rc;
    CornipickleParser parser;

    @Before
    public void setUp(){
        parser = new CornipickleParser();

        String line = "match $x's width with \"[0-9]\"";

        rc = (RegexCapture)UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<regex_capture>");
    }


    @Test
    public void testToString(){
        String expected = "match $x's width with [0-9]";
        assertTrue(expected.equals(rc.toString()));
    }

    @Test
    public void testGetClone(){
        RegexCapture clone = rc.getClone();
        boolean variable = rc.getVariable().equals(clone.getVariable());
        boolean pattern = rc.getPattern().equals(clone.getPattern());
        assertTrue(variable&&pattern);
    }

    @Test
    public void testSetPattern(){
        rc.setPattern(Pattern.compile("[0-1]"));
        assertTrue(rc.getPattern().equals("[0-1]"));
    }
	@Test
	public void testEvaluateJsonElementMapOfStringJsonElement2() {			
		JsonElement js =new JsonString("text");
		JsonString test = new JsonString("text63216321");
		JsonMap jm=new JsonMap();
		jm.put("text", test);
		Map<String, JsonElement>map=new HashMap<String,JsonElement>();
		map.put("JsonMap", jm);
		rc.m_variable.m_propertyName="text";
		rc.m_variable.m_elementName="JsonMap";
		assertTrue(rc.evaluate(js, map).toString().equals("[]"));
		
	}



}

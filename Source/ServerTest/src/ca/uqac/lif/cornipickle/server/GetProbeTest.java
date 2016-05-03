package ca.uqac.lif.cornipickle.server;

import ca.uqac.lif.cornipickle.Interpreter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paul on 03/05/16.
 */
public class GetProbeTest {
    
    @Test
    public void TestGetProbeEscapeString(){
        String result = GetProbe.escapeString("test\\test\ntest\r");
        String expected = "test\\test\\ntest\\r";
        assertTrue(expected.equals(result));
    }
    
}

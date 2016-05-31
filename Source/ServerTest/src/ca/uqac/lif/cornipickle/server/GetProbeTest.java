package ca.uqac.lif.cornipickle.server;

import org.junit.Test;
import static org.junit.Assert.*;

public class GetProbeTest {
    
    @Test
    public void TestGetProbeEscapeString(){
        String result = GetProbe.escapeString("test\\test\ntest\r");
        String expected = "test\\test\\ntest\\r";
        assertTrue(expected.equals(result));
    }
    
}

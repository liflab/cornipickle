package ca.uqac.lif.cornipickle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatementMetadataTest {
    Interpreter.StatementMetadata sm;
    @Before
    public void setUp() {
       sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");
    }

    @Test
    public void StatementMetadataTestToString(){
        String s1 = "@size Size of the element";
        String s2="@name Name of the element";
        assertTrue(sm.toString().contains(s1)&&sm.toString().contains(s2));
    }


}

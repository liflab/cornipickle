package ca.uqac.lif.cornipickle;/**
 * Created by paul on 13/05/16.
 */

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
        String expected = "@size Size of the element\n" +
                "@name Name of the element\n";
        assertTrue(expected.equals(sm.toString()));
    }


}

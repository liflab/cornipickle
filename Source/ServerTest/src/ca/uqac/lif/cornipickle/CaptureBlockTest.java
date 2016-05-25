package ca.uqac.lif.cornipickle;/**
 * Created by paul on 11/05/16.
 */

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CaptureBlockTest {

    @Before
    public void setUp() {

    }

    @Test
    public void CaptureBlockTestDefaultConstructor(){
        CaptureBlock cb = new CaptureBlock();
        assertTrue(cb instanceof CaptureBlock);
    }

    @Test
    public void CaptureBlockTestConstructor(){
        CaptureBlock cb = new CaptureBlock("string");
        assertTrue(cb instanceof CaptureBlock && ((StringConstant)cb).toString().equals("string"));
    }


}

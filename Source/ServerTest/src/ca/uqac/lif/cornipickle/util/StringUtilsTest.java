package ca.uqac.lif.cornipickle.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class StringUtilsTest {

    @Before
    public void setUp(){

    }


    @Test
    public void TestStringUtilsTestPrepend1(){

        String prependString = "PREP";
        String multiLineString = "ligne\nligne\nligne";

        String out = StringUtils.prepend(prependString, multiLineString);

        String expected = "PREPligne\nPREPligne\nPREPligne\n";

        assertTrue(out.equals(expected));

    }

    @Test
    public void TestStringUtilsTestPrepend2(){

        String prependString = "PREP";
        StringBuilder sb = new StringBuilder().append("ligne\n" +
                "ligne\n" +
                "ligne");

        String out = StringUtils.prepend(prependString, sb);

        String expected = "PREPligne\nPREPligne\nPREPligne\n";

        assertTrue(out.equals(expected));

    }



    @Test
    public void TestWordWrap1(){
        StringBuilder sb = new StringBuilder().append("ligne ligne");
        String out = StringUtils.wordWrap(sb, 6, "\n");

        String expected = "ligne\nligne";

        assertTrue(expected.equals(out));
    }


    @Test
    public void TestWordWrap2(){
        StringBuilder sb = new StringBuilder().append("ligne ligne");
        String out = StringUtils.wordWrap(sb, 6);

        String expected = "ligne\nligne";

        assertTrue(expected.equals(out));
    }

    @Test
    public void TestWordWrap3(){
        String s ="ligne ligne";
        String out = StringUtils.wordWrap(s, 6);

        String expected = "ligne\nligne";

        assertTrue(expected.equals(out));
    }



}

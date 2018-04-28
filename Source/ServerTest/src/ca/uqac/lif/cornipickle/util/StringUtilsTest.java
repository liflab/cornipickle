/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2018 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

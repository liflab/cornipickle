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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class AnsiPrinterTest
{
	AnsiPrinter ansiPrinter;

	@Before
    public void setUp() throws FileNotFoundException
	{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        ansiPrinter = new AnsiPrinter(fos);
    }

    @Test
    public void TestAnsiPrinterEnableColors()
    {
        ansiPrinter.enableColors();
        assertTrue(ansiPrinter.m_enabled);
    }


    @Test
    public void TestAnsiPrinterDisableColors() throws Exception{
        ansiPrinter.disableColors();
        assertFalse(ansiPrinter.m_enabled);
    }




    @Test
    public void TestAnsiPrinterResetColors()throws Exception{
        ansiPrinter.resetColors();
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }
        assertEquals("\u001B[0m\u001B[39m\u001B[49m", sb.toString());
        fis.close();
    }





    @Test
    public void TestAnsiPrinterSetForegroundColorNull()throws Exception{
        ansiPrinter.disableColors();
        //assertEquals(ansiPrinter.setForegroundColor(AnsiPrinter.Color.BLACK),);
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorBLACK()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.BLACK);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[0;30m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorRED()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.RED);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[0;31m", sb.toString());
        fis.close();
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorGREEN()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.GREEN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }
        assertEquals("\u001B[0;32m", sb.toString());
        fis.close();
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorBROWN()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.BROWN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[0;33m", sb.toString());
        fis.close();
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorBLUE()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.BLUE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[0;34m", sb.toString());
        fis.close();
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorPURPLE()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.PURPLE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[0;35m", sb.toString());
        fis.close();
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorCYAN()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.CYAN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[0;36m", sb.toString());
        fis.close();
    }


    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_GRAY()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_GRAY);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[0;37m", sb.toString());
        fis.close();
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorDARK_GRAY()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.DARK_GRAY);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;30m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_RED()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_RED);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;31m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_GREEN()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_GREEN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;32m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorYELLOW()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.YELLOW);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;33m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_BLUE()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_BLUE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;34m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_PURPLE()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_PURPLE);
        FileInputStream fis = new FileInputStream("temp.tmp");
        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;35m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_CYAN()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_CYAN);
        FileInputStream fis = new FileInputStream("temp.tmp");
        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;36m", sb.toString());
        fis.close();
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorWHITE()throws Exception{
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.WHITE);
        FileInputStream fis = new FileInputStream("temp.tmp");
        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals("\u001B[1;37m", sb.toString());
        fis.close();
    }
}

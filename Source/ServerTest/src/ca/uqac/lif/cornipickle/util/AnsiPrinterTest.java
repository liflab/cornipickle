package ca.uqac.lif.cornipickle.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AnsiPrinterTest {
	
	AnsiPrinter ansiPrinter;

	@Before
    public void setUp() throws  Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        ansiPrinter = new AnsiPrinter(fos);
    }

    @Test
    public void TestAnsiPrinterEnableColors() throws Exception{
        ansiPrinter.enableColors();
        assertTrue(ansiPrinter.m_enabled==true);
    }


    @Test
    public void TestAnsiPrinterDisableColors() throws Exception{
        ansiPrinter.disableColors();
        assertTrue(ansiPrinter.m_enabled==false);
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
        assertEquals(sb.toString(), "\u001B[0;37m");
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

        assertEquals(sb.toString(), "\u001B[0;30m");
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

        assertEquals(sb.toString(), "\u001B[0;31m");
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
        assertEquals(sb.toString(), "\u001B[0;32m");
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

        assertEquals(sb.toString(), "\u001B[0;33m");
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

        assertEquals(sb.toString(), "\u001B[0;34m");
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

        assertEquals(sb.toString(), "\u001B[0;35m");
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

        assertEquals(sb.toString(), "\u001B[0;36m");
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

        assertEquals(sb.toString(), "\u001B[0;37m");
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

        assertEquals(sb.toString(), "\u001B[1;30m");
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

        assertEquals(sb.toString(), "\u001B[1;31m");
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

        assertEquals(sb.toString(), "\u001B[1;32m");
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

        assertEquals(sb.toString(), "\u001B[1;33m");
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

        assertEquals(sb.toString(), "\u001B[1;34m");
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

        assertEquals(sb.toString(), "\u001B[1;35m");
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

        assertEquals(sb.toString(), "\u001B[1;36m");
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

        assertEquals(sb.toString(), "\u001B[1;37m");
        fis.close();
    }
}

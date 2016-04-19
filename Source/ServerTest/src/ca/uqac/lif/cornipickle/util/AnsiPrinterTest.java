package ca.uqac.lif.cornipickle.util;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by paul on 18/04/16.
 */
public class AnsiPrinterTest {

    @Before
    public void setUp() throws  Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
    }

    @Test
    public void TestAnsiPrinterEnableColors() throws Exception{
        AnsiPrinter ansiPrinter = new AnsiPrinter(new FileOutputStream("temp.tmp"));
        ansiPrinter.enableColors();
        assertTrue(ansiPrinter.m_enabled==true);
    }


    @Test
    public void TestAnsiPrinterDisableColors() throws Exception{
        AnsiPrinter ansiPrinter = new AnsiPrinter(new FileOutputStream("temp.tmp"));
        ansiPrinter.disableColors();
        assertTrue(ansiPrinter.m_enabled==false);
    }




    @Test
    public void TestAnsiPrinterResetColors()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.resetColors();
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;37m");
    }





    @Test
    public void TestAnsiPrinterSetForegroundColorNull()throws Exception{
        AnsiPrinter ansiPrinter = new AnsiPrinter(new FileOutputStream("temp.tmp"));
        ansiPrinter.disableColors();
        //assertEquals(ansiPrinter.setForegroundColor(AnsiPrinter.Color.BLACK),);
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorBLACK()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.BLACK);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;30m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorRED()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.RED);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;31m");
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorGREEN()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.GREEN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;32m");
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorBROWN()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.BROWN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;33m");
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorBLUE()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.BLUE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;34m");
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorPURPLE()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.PURPLE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;35m");
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorCYAN()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.CYAN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;36m");
    }


    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_GRAY()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_GRAY);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[0;37m");
    }



    @Test
    public void TestAnsiPrinterSetForeGroundColorDARK_GRAY()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.DARK_GRAY);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;30m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_RED()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_RED);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;31m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_GREEN()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_GREEN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;32m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorYELLOW()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.YELLOW);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;33m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_BLUE()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_BLUE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;34m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_PURPLE()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_PURPLE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;35m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorLIGHT_CYAN()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.LIGHT_CYAN);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;36m");
    }

    @Test
    public void TestAnsiPrinterSetForeGroundColorWHITE()throws Exception{
        FileOutputStream fos = new FileOutputStream("temp.tmp");
        AnsiPrinter ansiPrinter = new AnsiPrinter(fos);
        ansiPrinter.setForegroundColor(AnsiPrinter.Color.WHITE);
        FileInputStream fis = new FileInputStream("temp.tmp");

        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = fis.read())!=-1){
            sb.append((char)ch);
        }

        assertEquals(sb.toString(), "\u001B[1;37m");
    }









}

package ca.uqac.lif.cornipickle.server;

import ca.uqac.lif.cornipickle.Interpreter;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;


public class CornipickleServerTest {

    @Test
    public void estCornipickleServerConstructor(){

        CornipickleServer cs = new CornipickleServer("server", 1234);

        String serverName = cs.getServerName();
        int serverPort = cs.getServerPort();


        assertTrue(serverPort==1234&&"server".equals(serverName));
    }

    @Test
    public void testCornipickleServerSetInterpreter(){

        CornipickleServer cs = new CornipickleServer("server", 1234);

        Interpreter i = new Interpreter();

        cs.setInterpreter(i);

        assertTrue(cs.m_interpreter==i);

    }
    
    @Test
    public void testCornipickleServerLastProbeContact(){

        CornipickleServer cs = new CornipickleServer("server", 1234);

        cs.setLastProbeContact();
        Date lastProbeContact = cs.m_lastProbeContact;

        assertTrue(lastProbeContact==cs.getLastProbeContact());

    }
    
    @Test
    public void testCornipickleEspaceQuotes(){

        String result = CornipickleServer.escapeQuotes("\"word\"");
        String expected = "\"word\"".replaceAll("\"", "\\\\\"");

        assertTrue(expected.equals(result));

    }

    @Test
    public void testCornipickleServerReadProperties(){
        //TODO
    }


}

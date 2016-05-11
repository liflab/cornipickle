package ca.uqac.lif.cornipickle.server;/**
 * Created by paul on 06/05/16.
 */

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Verdict;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DummyImageTest {

    byte[] red;
    byte[] out;

    @Before
    public void setUp() {
        red = DummyImage.s_dummyImageRed;
        out = DummyImage.s_dummyImage;
    }

    @Test
    public void DummyImageTestSelectImageError(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");

        verdicts.put(sm, new Verdict(Verdict.Value.FALSE));
        byte[] result= DummyImage.selectImage(verdicts);

        assertArrayEquals(result, red);

    }

    @Test
    public void DummyImageTestSelectImageOK(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");

        verdicts.put(sm, new Verdict(Verdict.Value.TRUE));
        byte[] result= DummyImage.selectImage(verdicts);

        assertArrayEquals(result, out);

    }

    @Test
    public void DummyImageTestSelectImageEmptyVerdicts(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();

        byte[] result= DummyImage.selectImage(verdicts);

        assertArrayEquals(result, out);

    }
    
    
    @Test
    public void DummyImageTestCreateResponseCookie(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");
        sm.put("description", "Description of the element");

        verdicts.put(sm, new Verdict(Verdict.Value.TRUE));

        Interpreter.StatementMetadata sm2 = new Interpreter.StatementMetadata();

        sm2.put("name", "Name of the element2");
        sm2.put("size", "Size of the element2");
        sm.put("description", "Description of the element2");

        verdicts.put(sm2, new Verdict(Verdict.Value.FALSE));

        Interpreter.StatementMetadata sm3 = new Interpreter.StatementMetadata();

        sm3.put("name", "Name of the element3");
        sm.put("description", "Description of the element3");

        verdicts.put(sm3, new Verdict(Verdict.Value.INCONCLUSIVE));


        Interpreter i = new Interpreter();


        String result = DummyImage.createResponseCookie(verdicts, i.toString());

        boolean v1 = result.contains("\"num-false\":0");


        System.out.println(result);




    }





}

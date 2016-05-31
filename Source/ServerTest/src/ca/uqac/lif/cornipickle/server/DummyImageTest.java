package ca.uqac.lif.cornipickle.server;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.Witness;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
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
    public void DummyImageTestGetIdsToHighlight(){
        JsonMap jm = new JsonMap();
        jm.put("cornipickleid", new JsonNumber(1));

        JsonMap jm2 =new JsonMap();
        jm2.put("cornipickleid", new JsonNumber(2));

        Witness w = new Witness(jm);
        Witness w2 = new Witness((jm2));

        Verdict v = new Verdict(Verdict.Value.FALSE);
        v.setWitnessFalse(w);
        v.setWitnessTrue(w2);

        assertTrue(DummyImage.getIdsToHighlight(v).toString().contains("1")&&!DummyImage.getIdsToHighlight(v).toString().contains("2"));
    }

    @Test
    public void DummyImageTestGetIdsToHighlight2(){
       JsonNumber jn = new JsonNumber(1);
        Witness w2 = new Witness((jn));
        Verdict v = new Verdict(Verdict.Value.FALSE);
        v.setWitnessTrue(w2);
        assertTrue(DummyImage.getIdsToHighlight(v).toString().equals("[[]]"));
    }


    @Test
    public void DummyImageTestGetIdsToHighlight3(){
        JsonMap jm = new JsonMap();
        jm.put("cornipickleid", new JsonString("string"));
        Witness w = new Witness(jm);
        Verdict v = new Verdict(Verdict.Value.FALSE);
        v.setWitnessFalse(w);

        assertTrue(DummyImage.getIdsToHighlight(v).toString().equals("[[]]"));
    }



    
    
    
    
    
    
    
}

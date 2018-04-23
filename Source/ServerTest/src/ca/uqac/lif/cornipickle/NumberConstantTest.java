package ca.uqac.lif.cornipickle;
import ca.uqac.lif.json.JsonNumber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberConstantTest {

    @Before
    public void setUp() {
        
    }
    
    @Test
    public void NumberConstantDefaultConstructor(){
        NumberConstant nc = new NumberConstant();
        assertTrue(nc.m_value==null);
    }

    @Test
    public void NumberConstantTestNumberConstructor()
    {
        NumberConstant nc = new NumberConstant(new Number() {
            /**
			 * Dummy UID
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }
        });

        assertTrue(nc.m_value.numberValue().intValue()==0);

    }
    
    @Test
    public void NumberConstantTestStringConstructor(){
        NumberConstant nc = new NumberConstant("1");
        assertTrue(nc.m_value.numberValue().intValue()==1);
    }
    
    @Test
    public void NumberConstantTestJsonNumberConstructor(){
        NumberConstant nc = new NumberConstant(new JsonNumber(new Number() {
            /**
			 * Dummy UID
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }
        }));

        assertTrue(nc.m_value.numberValue().intValue()==0);


    }

    @Test
    public void NumberConstantTestToString(){
        NumberConstant nc = new NumberConstant("1");
        assertTrue(nc.toString().equals("1.0"));
    }


}

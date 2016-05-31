package ca.uqac.lif.cornipickle;

import ca.uqac.lif.bullwinkle.ParseNode;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ElementListTest {

    ElementList el;
    CornipickleParser parser;
    Collection<LanguageElement> c;
    AndStatement as;


    @Before
    public void setUp() {
        el = new ElementList();

        parser = new CornipickleParser();
        String line = "($y's right is greater than $x's left)\n" +
                "  And\n" +
                "  ($x's right is greater than $y's left)";

        ParseNode pn = UtilsTest.shouldParseAndNotNull(parser, line, "<and>");
        LanguageElement e = parser.parseStatement(pn);

        if (e == null)
        {
            fail("Parsing returned null");
        }
        if (!(e instanceof AndStatement))
        {
            fail("Got wrong type of object");
        }

        as = (AndStatement)e;

        el.add(as);




        c = new LinkedList<LanguageElement>();
        c.add(as);
        c.add(as);

    }
    
    
    @Test
    public void ElementListTestToString(){
        String expected = "[($y's right is greater than $x's left) And ($x's right is greater than $y's left)]";
        assertTrue(expected.equals(el.toString()));
    }
    
    @Test
    public void ElementListTestAdd(){
        String line = "Eventually ( \"3\" equals \"3\")\n";

        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");

        boolean addResult = el.add(element);

        boolean insertedElement = el.m_list.get(1).equals(element);


        assertTrue(addResult&&insertedElement);

    }

    @Test
    public void ElementListTestAddIndex(){
        String line = "Eventually ( \"3\" equals \"3\")\n";

        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(0,element);

        boolean insertedElement = el.m_list.get(0).equals(element);


        assertTrue(insertedElement);
    }
    
    @Test
    public void ElementListTestClear(){
        el.clear();
        assertTrue(el.isEmpty());
    }
    
    @Test
    public void ElementListTestContains(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);
        assertTrue(el.m_list.contains(element));
    }
    
    @Test
    public void ElementListTestGet(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);

        assertTrue(el.get(1).equals(element));
    }
    
    @Test
    public void ElementListTestIndexOf(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);

        assertTrue(el.indexOf(element)==1);
    }

    @Test
    public void ElementListTestIsEmpty(){
        boolean b1 = el.isEmpty();
        el.m_list.remove(0);
        boolean b2 = el.isEmpty();
        assertTrue(!b1&&b2);
    }

    @Test
    public void ElementListTestLastIndexOf(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);
        el.add(element);
        el.add(element);

        assertTrue(el.lastIndexOf(element)==3);
    }
    
    @Test
    public void ElementListTestRemoveElement(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);

        el.remove(element);

        assertTrue(!el.contains(element));

    }

    @Test
    public void ElementListTestRemoveIndex(){
        el.remove(0);
        assertTrue(el.isEmpty());
    }
    
    @Test
    public void ElementListTestSet(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.set(0, element);
        assertTrue(el.get(0).equals(element));
    }
    
    @Test
    public void ElementListTestSize(){
        assertTrue(el.size()==1);
    }

    @Test
    public void ElementListTestAddAll(){

        el.addAll(c);
        boolean b1 = el.get(1).equals(as);
        boolean b2 = el.get(2).equals(as);

        assertTrue(b1&&b2);

    }

    @Test
    public void ElementListTestAddAllIndex(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);

        el.addAll(1, c);
        boolean b1 = el.get(1).equals(as);
        boolean b2 = el.get(2).equals(as);
        boolean b3 = el.get(3).equals(element);

        assertTrue(b1&&b2&&b3);

    }

    @Test
    public void ElementListTestContainsAll(){
        el.addAll(c);
        assertTrue(el.m_list.containsAll(c));
    }
    
    @Test
    public void ElementListTestRemoveAll(){
        el.addAll(c);
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);
        el.removeAll(c);

        assertTrue(el.get(0).equals(element));
    }
    
    @Test
    public void ElementListTestSubList(){
        String line = "Eventually ( \"3\" equals \"3\")\n";
        LanguageElement element = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<eventually>");
        el.add(element);
        el.addAll(c);

        List<LanguageElement> list = el.subList(1,3);

        assertTrue(list.contains(element)&&list.contains(as));


    }
	@Test
	public void testpostfixaccept(){
		LanguageElementVisitor lev =new AttributeExtractor();
		el.postfixAccept(lev);
		assertTrue(true);		
	}
	@Test
	public void testprefixaccept(){
		LanguageElementVisitor lev =new AttributeExtractor();
		el.prefixAccept(lev);
		assertTrue(true);		
	}



    


}

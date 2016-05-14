package ca.uqac.lif.cornipickle;/**
 * Created by paul on 13/05/16.
 */

import org.jcp.xml.dsig.internal.dom.Utils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InterpreterTest {


    Interpreter i;


    @Before
    public void setUp() {
        i = new Interpreter();
    }
    
    @Test
    public void InterpreterTestSaveAndRestoreMemento(){
        String s = i.saveToMemento();
        Interpreter i2 = i.restoreFromMemento(s);

        assertTrue(i.saveToMemento().equals(i2.saveToMemento()));
    }
    
    @Test
    public void InterpreterTestGetPredicates() throws CornipickleParser.ParseException {
        PredicateDefinition pd = (PredicateDefinition)UtilsTest.shouldParseAndNotNullReturnElement(i.m_parser, "We say that $x and $y are top-aligned when (\n" +
                "  $x's top equals $y's top\n" +
                ")", "<predicate>");
        i.parseProperties("We say that $x and $y are top-aligned when ( $x's top equals $y's top ).");
        assertTrue(i.getPredicates().toString().contains(pd.toString()));
    }

    @Test
    public void InterpreterTestGetProperty() throws CornipickleParser.ParseException {
        //ElementPropertyComplement epc = (ElementPropertyComplement)UtilsTest.shouldParseAndNotNullReturnElement(i.m_parser, "", "<elem_property>");
        i.parseProperties("Eventually ( \"3\" equals \"3\").\nEventually ( \"3\" equals \"3\").\nEventually ( \"3\" equals \"3\").");

        System.out.println(i.m_statements);
    }


}

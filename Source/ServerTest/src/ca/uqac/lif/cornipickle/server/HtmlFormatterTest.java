package ca.uqac.lif.cornipickle.server;


import ca.uqac.lif.cornipickle.*;

import ca.uqac.lif.cornipickle.util.StringUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import static org.junit.Assert.*;


public class HtmlFormatterTest {


    CornipickleParser parser;
    HtmlFormatter hf;

    @Before
    public void setUp(){
        parser = new CornipickleParser();
        hf = new HtmlFormatter();
    }

    @Test
    public void htmlFormatterTestConstructor(){

        HtmlFormatter hf = new HtmlFormatter();
        assertTrue(hf.m_elements.isEmpty());

    }


    @Test
    public void htmlFormatterTestVisitElementNull(){
        HtmlFormatter hf = new HtmlFormatter();
        hf.visit(null);
    }


    @Test
    public void htmlFormatterTestVisitElementStringConstant(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        StringConstant sc = new StringConstant("constant");
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"string-constant\">").append("\"").append(sc.toString()).append("\"");
        String expected = out.toString();
        //Visit
        hf.visit(sc);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }


    @Test
    public void htmlFormatterTestVisitElementNumberConstant(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        NumberConstant nc = new NumberConstant(1);
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"number-constant\">").append(nc.toString());
        String expected = out.toString();
        //Visit
        hf.visit(nc);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }


    @Test
    public void htmlFormatterTestVisitElementPropertyPossessive(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        ElementPropertyPossessive epp = new ElementPropertyPossessive();
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"element-property\">");
        out.append("<span class=\"element-name\">").append(epp.getElementName()).append("</span>");
        out.append("'s ");
        out.append("<span class=\"property-name\">").append(epp.getPropertyName()).append("</span>");
        String expected = out.toString();
        //Visit
        hf.visit(epp);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }


    @Test
    public void htmlFormatterTestVisitElementPropertyComplement() throws CornipickleParser.ParseException {
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        String line = "the height of $x";
        LanguageElement e = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<elem_property_com>" );
        ElementPropertyComplement epc = (ElementPropertyComplement)e;


        StringBuilder out = new StringBuilder();
        out.append("<span class=\"element-property\">");
        out.append("the <span class=\"property-name\">").append(epc.getPropertyName()).append("</span>");
        out.append(" of ");
        out.append("<span class=\"element-name\">").append(epc.getElementName()).append("</span>");
        String expected = out.toString();
        //Visit
        hf.visit(epc);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }





    /*
    @Test
    public void htmlFormatterTestVisitElementComparisonStatement() throws CornipickleParser.ParseException {

        String line = "\"3\" is less than \"3\"\n";

        LanguageElement e  = UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<lt>");
        ComparisonStatement cs = (ComparisonStatement)e;
        hf.visit(cs);

        System.out.println(cs);



    }*/



    @Test
    public void htmlFormatterTestVisitElementImpliesStatement(){



        //Element
        ImpliesStatement is =(ImpliesStatement) UtilsTest.shouldParseAndNotNullReturnElement(parser, "If ( \"3\" equals \"3\") Then ( \"3\" equals \"3\")\n", "<implies>");
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"implies\">If (<br />\n");
        StringBuilder left = new StringBuilder("3 equals 3"); // Inner statement
        StringBuilder right = new StringBuilder("3 equals 3"); // Property
        out.append(StringUtils.prepend("&nbsp;", left));
        out.append("<br />\n)<br/>\nThen (<br />\n");
        out.append(StringUtils.prepend("&nbsp;", right));
        out.append("<br />\n)");
        String expected = out.toString();
        //Visit

        hf.m_elements.add(new StringBuilder(is.getStatements().get(0).toString()));
        hf.m_elements.add(new StringBuilder(is.getStatements().get(1).toString()));

        hf.visit(is);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peek().toString()));

    }







    @Test
    public void htmlFormatterTestVisitElementNextTime() {
        HtmlFormatter hf = new HtmlFormatter();

        StringBuilder sb1 = new StringBuilder("e1");
        StringBuilder sb2 = new StringBuilder("e2");

        hf.m_elements.push(sb1);
        hf.m_elements.push(sb2);
        //System.out.println(sb1.toString());

        //Element
        NextTime nt = new NextTime();
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"temporal-operator\">The next time (<br />\n");
        StringBuilder right = sb2;
        StringBuilder left = sb1;
        out.append(StringUtils.prepend("&nbsp;", left));
        out.append("<br />\n)<br/>\nThen (<br />\n");
        out.append(StringUtils.prepend("&nbsp;", right));
        out.append("<br />\n)");
        String expected = out.toString();
        //Visit
        hf.visit(nt);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));
    }


    @Test
    public void htmlFormatterTestVisitElementNAryStatement() {
        HtmlFormatter hf = new HtmlFormatter();



        //Element
        NAryStatement ns = new AndStatement();
        StringBuilder out = new StringBuilder();


        out.append("<span class=\"nary-statement\">");
        String keyword = ns.getKeyword();
        for (int i = 0; i < ns.getStatements().size(); i++)
        {
            if (i > 0)
            {
                out.append("<br/>\n").append(keyword).append("<br/>\n");
            }
            StringBuilder sts = hf.m_elements.pop();
            sts.insert(0, "(");
            sts.append(")");
            //sts = prepend("&nbsp;", sts);
            out.append(sts);
        }


        String expected = out.toString();
        //Visit
        hf.visit(ns);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));
    }



    @Test
    public void htmlFormatterTestVisitElementRegexCapture() {
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        RegexCapture rc = (RegexCapture) UtilsTest.shouldParseAndNotNullReturnElement(parser, "match $x's width with \"[0-9]\"", "<regex_capture>");
        String expected = "<span class=\"regex-capture\">match <span class=\"element-name\">$x's width</span> with "+"<span class=\"string\">\"[0-9]\"</span>";

        hf.visit(rc);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peek().toString()));
    }






    //NullPointerException


    @Test
    public void htmlFormatterTestVisitElementExistsStatement(){
        HtmlFormatter hf = new HtmlFormatter();
        ExistsStatement es = (ExistsStatement)UtilsTest.shouldParseAndNotNullReturnElement(parser, "There exists $d in $(#d) such that ( $d's width equals (200 + 100) )" , "<exists>");

        hf.m_elements.push(new StringBuilder(es.getStatement().toString()));
        hf.m_elements.push(new StringBuilder("$(#d)"));

        //Element
        StringBuilder out = new StringBuilder();

        out.append("<span class=\"exists\">There exists ");
        out.append("<span class=\"element-name\">").append(es.getVariable()).append("</span> in ");
        StringBuilder set_exp = new StringBuilder("$(#d)");
        out.append(set_exp);
        out.append(" such that (");
        StringBuilder inner_exp = new StringBuilder(es.getStatement().toString());
        out.append(inner_exp);
        out.append(" )");

        String expected = out.toString();
        //Visit
        hf.visit(es);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peek().toString()));

    }


    //NullPointerException
    /*
    @Test
    public void htmlFormatterTestVisitElementForAll() {
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        ForAllStatement fas = new ForAllStatement();
        StringBuilder out = new StringBuilder();

        StringBuilder sb1 = new StringBuilder("e1");
        StringBuilder sb2 = new StringBuilder("e2");

        hf.m_elements.push(sb1);
        hf.m_elements.push(sb2);

        out.append("<span class=\"forall\">For each ");
        out.append("<span class=\"element-name\">").append(fas.getVariable()).append("</span> in ");
        StringBuilder set_exp = sb1; // Set expression
        out.append(set_exp);
        out.append(" (<br/>\n");
        StringBuilder inner_exp = sb2; // Inner statement
        out.append(StringUtils.prepend("&nbsp;", inner_exp));
        out.append("<br/>\n)");

        String expected = out.toString();
        //Visit
        hf.visit(fas);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));
    }
    */



    @Test
    public void htmlFormatterTestVisitElementNegationStatement(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(sb);

        //Element
        NegationStatement ns = new NegationStatement();
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"negation\">");
        StringBuilder top = sb;
        out.append("Not (").append(top).append(")");
        String expected = out.toString();
        //Visit
        hf.visit(ns);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }


    @Test
    public void htmlFormatterTestVisitElementPredicateDefinition(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(sb);

        //Element
        PredicateDefinition pd = new PredicateDefinition(new StringConstant("e"));
        pd.setPattern(new StringConstant("p"));
        StringBuilder out = new StringBuilder();

        out.append("<span class=\"predicate-definition\">");
        out.append("We say that ");
        out.append(pd.getPattern());
        out.append(" when (<br />");
        StringBuilder pred = new StringBuilder("e");
        pred.append("\n<br/>)");
        out.append(StringUtils.prepend("&nbsp;", pred));

        String expected = out.toString();
        //Visit
        hf.visit(pd);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }



    @Test
    public void htmlFormatterTestVisitElementNext(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(sb);

        //Element
        Next ns = new Next();
        StringBuilder out = new StringBuilder();
        out.append("<span><span class=\"temporal-operator\">Next</span> (<br />\n");
        StringBuilder inner_exp = sb; // Inner statement
        out.append(StringUtils.prepend("&nbsp;", inner_exp));
        out.append("<br/>\n)");
        String expected = out.toString();
        //Visit
        hf.visit(ns);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }


    //Eventually

    @Test
    public void htmlFormatterTestVisitElementNever(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(sb);

        //Element
        Never n = new Never();
        StringBuilder out = new StringBuilder();

        out.append("<span><span class=\"temporal-operator\">Never</span> (<br />\n");
        StringBuilder inner_exp = sb; // Inner statement
        out.append(StringUtils.prepend("&nbsp;", inner_exp));
        out.append("<br/>\n)");

        String expected = out.toString();
        //Visit
        hf.visit(n);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }



    @Test
    public void htmlFormatterTestVisitElementGlobally(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(sb);

        //Element
        Globally g = new Globally();
        StringBuilder out = new StringBuilder();

        out.append("<span><span class=\"temporal-operator\">Always</span> (<br />\n");
        StringBuilder inner_exp = sb; // Inner statement
        out.append(StringUtils.prepend("&nbsp;", inner_exp));
        out.append("<br/>\n)");

        String expected = out.toString();
        //Visit
        hf.visit(g);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }



    @Test
    public void htmlFormatterTestVisitElementWhenIsNow(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(sb);

        //Element
        WhenIsNow win = new WhenIsNow("b", "a");
        StringBuilder out = new StringBuilder();

        out.append("<span class=\"forall\">When ");
        out.append("<span class=\"element-name\">").append(win.getVariableBefore()).append("</span> is now ");
        out.append("<span class=\"element-name\">").append(win.getVariableAfter()).append("</span>");
        out.append(" (<br/>\n");
        StringBuilder inner_exp = sb; // Inner statement
        out.append(StringUtils.prepend("&nbsp;", inner_exp));
        out.append("<br/>\n)");

        String expected = out.toString();
        //Visit
        hf.visit(win);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }



    @Test
    public void htmlFormatterTestVisitElementCssSelector(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        CssSelector cs = new CssSelector("e");
        StringBuilder out = new StringBuilder();

        out.append("<span class=\"css-selector\">");
        out.append("$(");
        out.append(cs.getSelector());
        out.append(")");

        String expected = out.toString();
        //Visit
        hf.visit(cs);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }

    @Test
    public void htmlFormatterTestVisitElementOther(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        LanguageElement e = new LanguageElement() {
            @Override
            public String toString(String indent) {
                return "element";
            }

            @Override
            public void prefixAccept(LanguageElementVisitor visitor) {

            }
            @Override
            public void postfixAccept(LanguageElementVisitor visitor) {

            }
        };
        StringBuilder out = new StringBuilder();

        out.append("<span>");

        String expected = out.toString();
        //Visit
        hf.visit(e);

        //Assert
        assertTrue(expected.equals(hf.m_elements.peekFirst().toString()));

    }



    @Test
    public void TestHtmlFormatterPop(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(new StringBuilder("e"));
        hf.pop();

        assertTrue((sb.toString()+"</span>").equals(hf.m_elements.peekFirst().toString()));


    }


    @Test
    public void HtmlFormatterTestEventually(){
        Eventually e = (Eventually) UtilsTest.shouldParseAndNotNullReturnElement(parser, "Eventually ( \"3\" equals \"3\")\n", "<enventually>");
        String expected = "<span><span class=\"temporal-operator\">Eventually</span> (<br />\n&nbsp;3 equals 3\n<br/>\n)";

        hf.m_elements.push(new StringBuilder("3 equals 3"));

        hf.visit(e);


        assertTrue(expected.equals(hf.m_elements.peek().toString()));
    }


    @Test
    public void HtmlFormatterTestForAllStatement(){
        ForAllStatement fas = (ForAllStatement)UtilsTest.shouldParseAndNotNullReturnElement(parser, "For each $x in $(p) ($x's width equals 100)", "<foreach>");
        String expected = "<span class=\"forall\">For each "+"<span class=\"element-name\">$x</span> in $(p) (<br/>\n&nbsp;$x's width equals 100\n<br/>\n)";

        hf.m_elements.push(new StringBuilder("$x's width equals 100"));
        hf.m_elements.push(new StringBuilder("$(p)"));

        hf.visit(fas);

        assertTrue(expected.equals(hf.m_elements.peek().toString()));
    }

    @Test
    public void HtmlFormatterTestFormatMetadata(){
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");



        String e1 = "&nbsp;&nbsp;"
                +"<span class=\"attribute-name\">@size</span> "
                +"Size of the element"
                +"\n<br />\n";


        String e2 = "&nbsp;&nbsp;"
                +"<span class=\"attribute-name\">@name</span> "
                +"Name of the element"
                +"\n<br />\n";
             
        String result = HtmlFormatter.format(sm);

        boolean assert1 = result.contains(e1);
        boolean assert2 = result.contains(e2);


        assertTrue(assert1&&assert2);


    }



    @Test
    public void HtmlFormatterTestFormatMetadataWithIgnoredAttributes(){
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");

        String expected = "<span class=\"comment\">\"\"\"<br/>\n"

                +"&nbsp;&nbsp;"
                +"<span class=\"attribute-name\">@name</span> "
                +"Name of the element"
                +"\n<br />\n"


                +"\"\"\"<br />\n</span>\n";

        HashSet<String> hs = new HashSet<String>();
        hs.add("size");

        String result = HtmlFormatter.format(sm,hs);

        assertTrue(expected.equals(result));

    }



    @Test
    public void HtmlFormatterTestFormatMetadataWithIgnoredAttributes2(){
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");

        String expected = "";

        HashSet<String> hs = new HashSet<String>();
        hs.add("size");
        hs.add("name");

        String result = HtmlFormatter.format(sm,hs);

        assertTrue(expected.equals(result));

    }

    /*@Test
    public void HtmlFormatterTestGetFormated(){
        ImpliesStatement is =(ImpliesStatement) UtilsTest.shouldParseAndNotNullReturnElement(parser, "If ( \"3\" equals \"3\") Then ( \"3\" equals \"3\")\n", "<implies>");
        System.out.println(hf.m_elements);
        hf.m_elements.clear();




        hf.getFormatted(is);



        //assertTrue(==null);
    }*/










}

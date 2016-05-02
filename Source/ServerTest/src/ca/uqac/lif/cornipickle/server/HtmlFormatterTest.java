package ca.uqac.lif.cornipickle.server;

import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.cornipickle.*;
import ca.uqac.lif.cornipickle.server.HtmlFormatter;
import ca.uqac.lif.cornipickle.util.StringUtils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paul on 02/05/16.
 */
public class HtmlFormatterTest {


    @Test
    public void htmlFormatterTestConstructor(){

        HtmlFormatter hf = new HtmlFormatter();
        assertTrue(hf.m_elements.empty());

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

    }

    /*
    @Test
    public void htmlFormatterTestVisitElementPropertyComplement(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        ElementPropertyComplement epc = new ElementPropertyComplement();
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"element-property\">");
        out.append("the <span class=\"property-name\">").append(e.getPropertyName()).append("</span>");
        out.append(" of ");
        out.append("<span class=\"element-name\">").append(e.getElementName()).append("</span>");
        String expected = out.toString();
        //Visit
        hf.visit(epp);

        //Assert
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

    }
    */


    /*
    @Test
    public void htmlFormatterTestVisitElementComparisonStatement(){


    }
    */

    /*
    @Test
    public void htmlFormatterTestVisitElementImpliesStatement(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        ImpliesStatement epc = new ImpliesStatement();
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"element-property\">");
        out.append("the <span class=\"property-name\">").append(e.getPropertyName()).append("</span>");
        out.append(" of ");
        out.append("<span class=\"element-name\">").append(e.getElementName()).append("</span>");
        String expected = out.toString();
        //Visit
        hf.visit(epp);

        //Assert
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

    }
    */


    /*
    @Test
    public void htmlFormatterTestVisitElementLetStatement(){
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        LetStatement epc = new LetStatement();
        StringBuilder out = new StringBuilder();
        out.append("<span class=\"element-property\">");
        out.append("the <span class=\"property-name\">").append(e.getPropertyName()).append("</span>");
        out.append(" of ");
        out.append("<span class=\"element-name\">").append(e.getElementName()).append("</span>");
        String expected = out.toString();
        //Visit
        hf.visit(epp);

        //Assert
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

    }
    */




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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));
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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));
    }


    /*
    @Test
    public void htmlFormatterTestVisitElementRegexCapture() {
        HtmlFormatter hf = new HtmlFormatter();

        //Element
        RegexCapture rc = new RegexCapture();
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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));
    }
    */





    //NullPointerException

    /*
    @Test
    public void htmlFormatterTestVisitElementExistsStatement(){
        HtmlFormatter hf = new HtmlFormatter();

        StringBuilder sb1 = new StringBuilder("e1");
        StringBuilder sb2 = new StringBuilder("e2");

        hf.m_elements.push(sb1);
        hf.m_elements.push(sb2);

        //Element
        ExistsStatement es = new ExistsStatement();
        StringBuilder out = new StringBuilder();

        out.append("<span class=\"exists\">There exists ");
        out.append("<span class=\"element-name\">").append(es.getVariable()).append("</span> in ");
        StringBuilder set_exp = sb2;
        out.append(set_exp);
        out.append(" such that (");
        StringBuilder inner_exp = sb1;
        out.append(inner_exp);
        out.append(" )");

        String expected = out.toString();
        //Visit
        hf.visit(es);

        //Assert
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

    }*/


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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));
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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

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
        assertTrue(expected.equals(hf.m_elements.firstElement().toString()));

    }



    @Test
    public void TestHtmlFormatterPop(){
        HtmlFormatter hf = new HtmlFormatter();
        StringBuilder sb = new StringBuilder("e");
        hf.m_elements.push(new StringBuilder("e"));
        hf.pop();

        assertTrue((sb.toString()+"</span>").equals(hf.m_elements.firstElement().toString()));


    }










}

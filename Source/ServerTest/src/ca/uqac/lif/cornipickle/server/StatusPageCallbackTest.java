package ca.uqac.lif.cornipickle.server;

import ca.uqac.lif.cornipickle.*;
import ca.uqac.lif.jerrydog.CallbackResponse;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

/**
 * Created by paul on 02/05/16.
 */
public class StatusPageCallbackTest {

    StatusPageCallback spc;
    CornipickleServer m_server;
    Interpreter i;
    @Before
    public void setUp(){
        m_server = new CornipickleServer("server", 1234);
         spc = new StatusPageCallback(new Interpreter(), m_server);
    }


    /*@Test
    public void testStatusPageCallbackTestProcess(){
        StatusPageCallback spc = new StatusPageCallback(new Interpreter(), new CornipickleServer("server", 1234));

        String response = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Cornipickle status</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"screen.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"colouring.css\" />\n" +
                "<script src=\"https://code.jquery.com/jquery-1.11.2.min.js\"></script>\n" +
                "<script src=\"https://code.jquery.com/ui/1.11.1/jquery-ui.min.js\"></script>\n" +
                "<script src=\"highlight.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Cornipickle Status</h1><p id=\"last-probe-contact\">No contact with probe so far.</p>\n" +
                "<p class=\"verdicts-empty\">Cornipickle has no property to evaluate.</p><h2 class=\"ui-accordion-header\">Properties</h2>\n" +
                "<p><a href=\"/reset\">Reset history</a></p>\n" +
                "<ul class=\"verdicts\">\n" +
                "</ul>\n" +
                "<div class=\"clearer\"></div>\n" +
                "<h2 class=\"ui-accordion-header\">Predicates</h2>\n" +
                "<ul class=\"predicates\">\n" +
                "</ul><div class=\"clearer\"></div>\n" +
                "<h2 class=\"ui-accordion-header\">Sets</h2>\n" +
                "<ul class=\"sets\">\n" +
                "</ul><div class=\"clearer\"></div>\n" +
                "\n" +
                "<div id=\"add-properties\">\n" +
                "<h2>Add properties</h2>\n" +
                "\n" +
                "<p>Type here the Cornipickle properties you want to add.</p>\n" +
                "<form method=\"post\" action=\"add\">\n" +
                "<div><textarea name=\"properties\" rows=\"10\" cols=\"40\"></textarea></div>\n" +
                "<input type=\"submit\" />\n" +
                "</form>\n" +
                "</div>\n" +
                "<div id=\"footer\">\n" +
                "<hr />\n" +
                new Date() +
                "</body>\n" +
                "</html>\n";

        System.out.println(response);

        CallbackResponse cr = spc.process() {
        })



    }*/

    /*@Test
    public void testStatusPageCallbackTestCreateSetList(){

        StatusPageCallback spc = new StatusPageCallback(new Interpreter(), new CornipickleServer("server", 1234));

        StringBuilder sb = new StringBuilder();

        SetDefinition sd1 = new SetDefinition("sd1");
        SetDefinition sd2 = new SetDefinition("sd2");
        SetDefinition sd3 = new SetDefinition("sd3");

        ArrayList<SetDefinition> alsd = new ArrayList<SetDefinition>();
        alsd.add(sd1);
        alsd.add(sd2);
        alsd.add(sd3);


        String expected = "<ul class=\"sets\">\n" +
                "<li>sd1</li>\n" +
                "<li>sd2</li>\n" +
                "<li>sd3</li>\n" +
                "</ul>";

        spc.createSetList(alsd, sb);

        System.out.println(expected);
        System.out.println(sb.toString());

        assertTrue(expected.equals(sb.toString()));






    }*/

    /*

    @Test
    public void testStatusPageCallbackTestCreatePredicateList(){

        PredicateDefinition pd1 = (PredicateDefinition)UtilsTest.shouldParseAndNotNullReturnElement(new CornipickleParser(), "We say that $x is thin when ($x's width equals 0)", "<predicate>");
        PredicateDefinition pd2 = (PredicateDefinition)UtilsTest.shouldParseAndNotNullReturnElement(new CornipickleParser(), "We say that $x is thin when ($x's width equals 0)", "<predicate>");
        PredicateDefinition pd3 = (PredicateDefinition)UtilsTest.shouldParseAndNotNullReturnElement(new CornipickleParser(), "We say that $x is thin when ($x's width equals 0)", "<predicate>");



        ArrayList<PredicateDefinition> alpd = new ArrayList<PredicateDefinition>();
        alpd.add(pd1);


        StringBuilder result = new StringBuilder();

        spc.createPredicateList(alpd, result);


        String expected = "<ul class=\"predicates\">\n"
                + "<li class=\"predicate-definition-item "
                +

        System.out.println(result);






    }*/


    @Test
    public void TestStatusPageCallbackCreateProbeContactMessageNull(){



        StringBuilder result = new StringBuilder();

        StatusPageCallback.createProbeContactMessage(null, result);

        String expected = "<p id=\"last-probe-contact\">No contact with probe so far.</p>\n";

        assertTrue(expected.equals(result.toString()));

    }


    @Test
    public void TestStatusPageCallbackCreateProbeContactMessageOk(){

        StringBuilder result = new StringBuilder();

        StatusPageCallback.createProbeContactMessage(new Date(), result);

        String expected = "<p id=\"last-probe-contact\">Last contact with probe <span id=\"time-string\">just now</span></p>\n";

        assertTrue(expected.equals(result.toString()));

    }

    @Test
    public void TestStatusPageCallBackFooter(){

        StringBuilder result = spc.pageFoot();

        Pattern pattern = Pattern.compile("<div id=\"footer\">\n<hr />\n.*</div>\n</body>\n</html>\n");
        Matcher matcher = pattern.matcher(result);

        assertTrue(matcher.matches());


    }


    @Test
    public void TestStatusPageCallBackHead(){



        StringBuilder result = spc.pageHead("title");

        String expected = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>title</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"screen.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"colouring.css\" />\n" +
                "<script src=\"https://code.jquery.com/jquery-1.11.2.min.js\"></script>\n" +
                "<script src=\"https://code.jquery.com/ui/1.11.1/jquery-ui.min.js\"></script>\n" +
                "<script src=\"highlight.js\"></script>\n" +
                "</head>\n<body>\n";

        assertTrue(expected.equals(result.toString()));

    }
    
    @Test
    public void StatusPageCallbackTestCreateStatusMessageOK(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");

        verdicts.put(sm, new Verdict(Verdict.Value.TRUE));


        StringBuilder result = new StringBuilder();
        StatusPageCallback.createStatusMessage(verdicts, result);

        String expected = "<p class=\"verdicts-no-error\">All's well! All properties evaluate to true.</p>";

        assertTrue(expected.equals(result.toString()));


    }

    @Test
    public void StatusPageCallbackTestCreateStatusMessageEmpty(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();


        StringBuilder result = new StringBuilder();
        StatusPageCallback.createStatusMessage(verdicts, result);

        String expected = "<p class=\"verdicts-empty\">Cornipickle has no property to evaluate.</p>";

        assertTrue(expected.equals(result.toString()));


    }


    @Test
    public void StatusPageCallbackTestCreateStatusMessage1Error(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");

        verdicts.put(sm, new Verdict(Verdict.Value.FALSE));


        StringBuilder result = new StringBuilder();
        StatusPageCallback.createStatusMessage(verdicts, result);

        String expected = "<p class=\"verdicts-errors\">Oops! There is a problem with some property.</p>";

        assertTrue(expected.equals(result.toString()));


    }

    @Test
    public void StatusPageCallbackTestCreateStatusMessageMultipleErrors(){
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");

        verdicts.put(sm, new Verdict(Verdict.Value.FALSE));



        Interpreter.StatementMetadata sm2 = new Interpreter.StatementMetadata();

        sm2.put("name2", "Name of the element2");
        sm2.put("size2", "Size of the element2");

        verdicts.put(sm2, new Verdict(Verdict.Value.FALSE));


        Interpreter.StatementMetadata sm3 = new Interpreter.StatementMetadata();

        sm3.put("name3", "Name of the element3");
        sm3.put("size3", "Size of the element3");

        verdicts.put(sm3, new Verdict(Verdict.Value.FALSE));


        StringBuilder result = new StringBuilder();

        StatusPageCallback.createStatusMessage(verdicts, result);

        String expected = "<p class=\"verdicts-errors\">Oops! There is a problem with 3 properties.</p>";

        assertTrue(expected.equals(result.toString()));


    }
    
    
    
    
    /*@Test
    public void StatusPageCallbackTestCreatePropertyList() throws CornipickleParser.ParseException {
        Map<Interpreter.StatementMetadata, Verdict> verdicts = new HashMap<Interpreter.StatementMetadata, Verdict>();
        Interpreter.StatementMetadata sm = new Interpreter.StatementMetadata();

        sm.put("name", "Name of the element");
        sm.put("size", "Size of the element");

        verdicts.put(sm, new Verdict(Verdict.Value.TRUE));

        StringBuilder result = new StringBuilder();



        i = new Interpreter();
        i.parseProperties("Name of the element");
        i.parseProperties("Size of the element");




        String s1 = "<li class=\"true\">"
                +"<div class=\"property-metadata\">"+HtmlFormatter.format(sm)+"</div>\n"
                +"<div class=\"property-contents\">\n"
                +HtmlFormatter.format(spc.m_interpreter.getProperty(sm));

        spc.m_interpreter.getProperty(sm);

        //spc.createPropertyList(verdicts, result);


    }*/





}

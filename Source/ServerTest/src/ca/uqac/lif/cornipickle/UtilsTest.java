package ca.uqac.lif.cornipickle;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import org.junit.Ignore;

import static org.junit.Assert.fail;

@Ignore
public class UtilsTest {

    public static ParseNode shouldParseAndNotNull(CornipickleParser parser,String line, String start_symbol)
    {
        BnfParser p = parser.getParser();
        //p.setDebugMode(true);
        p.setStartRule(start_symbol);
        ParseNode pn = null;

        try
        {
            pn = p.parse(line);
        } catch (BnfParser.ParseException e)
        {
            fail(e.toString());
        }
        if (pn == null)
        {
            fail("Failed parsing expression through grammar: returned null");
        }
        return pn;
    }

    public static LanguageElement shouldParseAndNotNullReturnElement(CornipickleParser parser,String line, String start_symbol){

        ParseNode pn = shouldParseAndNotNull(parser, line, start_symbol);

        LanguageElement e = parser.parseStatement(pn);

        return e;

    }

}

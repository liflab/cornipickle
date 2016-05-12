package ca.uqac.lif.cornipickle;/**
 * Created by paul on 11/05/16.
 */

import ca.uqac.lif.bullwinkle.ParseNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.*;

public class TagNameExtractorTest {

    TagNameExtractor tne;

    @Before
    public void setUp() {
        tne = new TagNameExtractor();
    }

    @Test
    public void TagNameExtractorTestConstructor(){
        assertTrue(tne.m_tags.isEmpty());
    }
    
    @Test
    public void TagNameExtractorTestGetTags(){

        CssSelector cs = new CssSelector(".classe1");
        CssSelector cs2 = new CssSelector("#id1");


        Set<String> set = TagNameExtractor.getTags(cs);
        set.addAll(TagNameExtractor.getTags(cs2));

        assertTrue(set.contains(".classe1")&&set.contains("#id1"));

    }

    @Test
    public void TagNameExtractorTestVisitPredicateCall(){

        CornipickleParser parser = new CornipickleParser();
        String line = "We say that $x and $y are top-aligned when (\n" +
                "  $x's top equals $y's top\n" +
                ")";
        PredicateDefinition pd = (PredicateDefinition)UtilsTest.shouldParseAndNotNullReturnElement(parser, line, "<predicate>");

        List<String> list = new ArrayList<String>();
        list.add("string1");
        list.add("string2");
        list.add("string3");

        PredicateCall pc = new PredicateCall(pd, "match", list);

        Set<String> set = TagNameExtractor.getTags(pc);

        assertTrue(set.isEmpty());


    }


}

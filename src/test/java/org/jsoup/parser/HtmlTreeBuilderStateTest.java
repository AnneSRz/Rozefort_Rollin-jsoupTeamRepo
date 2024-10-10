package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.HtmlTreeBuilderState.Constants;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartInputAttribs;
import static org.junit.jupiter.api.Assertions.*;

public class HtmlTreeBuilderStateTest {
    static List<Object[]> findConstantArrays(Class aClass) {
        ArrayList<Object[]> array = new ArrayList<>();
        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers) && field.getType().isArray()) {
                try {
                    array.add((Object[]) field.get(null));
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        return array;
    }

    static void ensureSorted(List<Object[]> constants) {
        for (Object[] array : constants) {
            Object[] copy = Arrays.copyOf(array, array.length);
            Arrays.sort(array);
            assertArrayEquals(array, copy);
        }
    }

    @Test
    public void ensureArraysAreSorted() {
        List<Object[]> constants = findConstantArrays(Constants.class);
        ensureSorted(constants);
        assertEquals(40, constants.size());
    }

    @Test public void ensureTagSearchesAreKnownTags() {
        List<Object[]> constants = findConstantArrays(Constants.class);
        for (Object[] constant : constants) {
            String[] tagNames = (String[]) constant;
            for (String tagName : tagNames) {
                if (StringUtil.inSorted(tagName, InBodyStartInputAttribs))
                    continue; // odd one out in the constant
                assertTrue(Tag.isKnownTag(tagName), String.format("Unknown tag name: %s", tagName));
            }
        }
    }


    @Test
    public void nestedAnchorElements01() {
        String html = "<html>\n" +
            "  <body>\n" +
            "    <a href='#1'>\n" +
            "        <div>\n" +
            "          <a href='#2'>child</a>\n" +
            "        </div>\n" +
            "    </a>\n" +
            "  </body>\n" +
            "</html>";
        String s = Jsoup.parse(html).toString();
        assertEquals("<html>\n" +
            " <head></head>\n" +
            " <body>\n" +
            "  <a href=\"#1\"> </a>\n" +
            "  <div>\n" +
            "   <a href=\"#1\"> </a><a href=\"#2\">child</a>\n" +
            "  </div>\n" +
            " </body>\n" +
            "</html>", s);
    }

    @Test
    public void nestedAnchorElements02() {
        String html = "<html>\n" +
            "  <body>\n" +
            "    <a href='#1'>\n" +
            "      <div>\n" +
            "        <div>\n" +
            "          <a href='#2'>child</a>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "    </a>\n" +
            "  </body>\n" +
            "</html>";
        String s = Jsoup.parse(html).toString();
        assertEquals("<html>\n" +
            " <head></head>\n" +
            " <body>\n" +
            "  <a href=\"#1\"> </a>\n" +
            "  <div>\n" +
            "   <a href=\"#1\"> </a>\n" +
            "   <div>\n" +
            "    <a href=\"#1\"> </a><a href=\"#2\">child</a>\n" +
            "   </div>\n" +
            "  </div>\n" +
            " </body>\n" +
            "</html>", s);
    }


        /*
    Test #3 : 
    Oracle :
    Intention : We want to test the case 'nobr' in method inBodyStartTag at line 448 of HtmlTreeBuilderState (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HtmlTreeBuilderState
    Utilité : Tester le comportement lors de l'ajout d'un nobr deprecated tag. Va fermer automatiquement les tags ouverts
    Exécution : Via ' mvn -Dtest=HtmlTreeBuilderStateTest1 test' ou Github Action
     */
    @Test
    public void inBodyStartTag() {

       //ARRANGE
        // html fragment that tests the swich case for the case 
       String html = 
       "<html>\n" +
       "  <body>\n" +
       "    <nobr>open nobr"+
       "    <nobr>nested nobr</nobr>\n" +
       "  </nobr>\n" +
       "</html>";
        //Act
        // Parse the body with the form tag
        Document doc = Jsoup.parseBodyFragment(html);
        Element body = doc.body();
        String parsed = body.html();
        System.out.println(parsed);
        // Assert
        String assertValue=
        "<nobr>open nobr </nobr>"+
        "<nobr>nested nobr</nobr>";
        assertEquals(assertValue, parsed);
    }

    /*
    Test #4 : 
    Oracle :
    Intention : We want to test the case 'colgroup' in method InColumnGroup at line 1207 of HtmlTreeBuilderState (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HtmlTreeBuilderState
    Utilité : Tester le comportement lors de l'ajout d'un nested colgroup tag. Va fermer automatiquement les tags ouverts et supprimer l'imbrication
    Exécution : Via ' mvn -Dtest=HtmlTreeBuilderStateTest1 test' ou Github Action
     */
    @Test
    public void inColumnGroup1() {

       //ARRANGE
        // part 1) broken table should be deleted
       String html1 = 
        "    <colgroup>\n"+
        "    <colgroup>\n"+
        "        <col>\n"+
        "    </colgroup>\n"+
        "    </colgroup>\n";

   
        //Act
        // Parse the body with the form tag
        Document doc = Jsoup.parseBodyFragment(html1);
        Element body = doc.body();
        String parsed = body.html();
        System.out.println(parsed);
        // Assert
        String assertValue1="";
   
        assertEquals(assertValue1, parsed);
    }

    @Test
    public void inColumnGroup2() {

       //ARRANGE
        // part 1) nested colgroups are illegal
       String html2 = 
        "<table>\n"+
        "<colgroup>\n"+
        "<colgroup>\n"+
        "<col>\n"+
        "</colgroup>\n"+
        "</colgroup>\n"+
        "<tbody>\n"+
        "</tbody>\n"+
        "</table>";
   
        //Act
        // Parse the body with the form tag
        Document doc = Jsoup.parseBodyFragment(html2);
        Element body = doc.body();
        String parsed = body.html();
        System.out.println(parsed);
        // Assert (nesting should have been resolved)
        String assertValue2=
       "<table>\n"+ 
       " <colgroup>\n"+
       " </colgroup>\n"+
       " <colgroup>\n"+
       "  <col>\n"+
       " </colgroup>\n"+
       " <tbody>\n"+
       " </tbody>\n"+
       "</table>";
   
        assertEquals(assertValue2, parsed);
    }




    /*
    Test #5 : Forbidden tags
    Oracle :
    Intention : We want to test the case 'DOCTYPE' in state InColumnGroup at line 1185 of HtmlTreeBuilderState (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HtmlTreeBuilderState
    Utilité : Tester le comportement lors de l'ajout d'un nested doctype tag. Va simplement supprimer le tag HTML, Doctype. Pour le tag illegal template, le parser le handle.
    Exécution : Via ' mvn -Dtest=HtmlTreeBuilderStateTest1 test' ou Github Action
     */
    @Test
    public void inColumnGroup3 () {

       //ARRANGE
        // part 1) if invalid table, we delete
       String html1 = 
       "<table>\n"+ 
       " <colgroup>\n"+
       "  <!DOCTYPE html>\n"+
       " </colgroup>\n"+
       " <tbody>\n"+
       " </tbody>\n"+
       "</table>";
   
        //Act
        // Parse the body with the form tag
        Document doc = Jsoup.parseBodyFragment(html1);
        Element body = doc.body();
        String parsed = body.html();
        System.out.println(parsed);
        // Assert
        String assertValue1=
        "<table>\n"+ 
        " <colgroup>\n"+
        " </colgroup>\n"+
        " <tbody>\n"+
        " </tbody>\n"+
        "</table>";
   
        assertEquals(assertValue1, parsed);
    }

    @Test
    public void inColumnGroup4 () {

        //ARRANGE
         // part 1) if invalid table, we delete
        String html1 = 
        "<table>\n"+ 
        " <colgroup>\n"+
        "  <html></html>\n"+
        " </colgroup>\n"+
        " <tbody>\n"+
        " </tbody>\n"+
        "</table>";
    
         //Act
         // Parse the body with the form tag
         Document doc = Jsoup.parseBodyFragment(html1);
         Element body = doc.body();
         String parsed = body.html();
         System.out.println(parsed);
         // Assert
         String assertValue1=
         "<table>\n"+ 
         " <colgroup>\n"+
         " </colgroup>\n"+
         " <tbody>\n"+
         " </tbody>\n"+
         "</table>";
    
         assertEquals(assertValue1, parsed);
     }

    @Test
    public void inColumnGroup5 () {

        //ARRANGE
        String html1 = 
        "<table>\n"+ 
        " <colgroup>\n"+
        "  <template>\n"+
        "   <p>illegal</p>\n"+
        "  </template>\n"+
        " </colgroup>\n"+
        " <tbody>\n"+
        " </tbody>\n"+
        "</table>";

        //Act
        // Parse the body with the form tag
        Document doc = Jsoup.parseBodyFragment(html1);
        Element body = doc.body();
        String parsed = body.html();
        System.out.println(parsed);
        // Assert
        String assertValue1=
        "<table>\n"+ 
        " <colgroup>\n"+
        "  <template>\n"+
        "   <p>illegal</p>\n"+
        "  </template>\n"+
        " </colgroup>\n"+
        " <tbody>\n"+
        " </tbody>\n"+
        "</table>";

        assertEquals(assertValue1, parsed);
    }

}
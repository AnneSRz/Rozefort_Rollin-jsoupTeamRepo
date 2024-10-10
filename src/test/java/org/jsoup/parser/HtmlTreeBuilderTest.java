package org.jsoup.parser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HtmlTreeBuilderTest {
    @Test
    public void ensureSearchArraysAreSorted() {
        List<Object[]> constants = HtmlTreeBuilderStateTest.findConstantArrays(HtmlTreeBuilder.class);
        HtmlTreeBuilderStateTest.ensureSorted(constants);
        assertEquals(10, constants.size());
    }

    @Test
    public void nonnull() {
        assertThrows(IllegalArgumentException.class, () -> {
                HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
                treeBuilder.parse(null, null, null); // not sure how to test that these visual warnings actually appear! - test below checks for method annotation
            }
        ); // I'm not convinced that this lambda is easier to read than the old Junit 4 @Test(expected=IEA.class)...
    }

    @Test public void nonnullAssertions() throws NoSuchMethodException {
        Annotation[] declaredAnnotations = TreeBuilder.class.getPackage().getDeclaredAnnotations();
        boolean seen = false;
        for (Annotation annotation : declaredAnnotations) {
            if (annotation.annotationType().isAssignableFrom(NullMarked.class))
                seen = true;
        }

        // would need to rework this if/when that annotation moves from the method to the class / package.
        assertTrue(seen);

    }
    /*
    Test #1 : 
    Oracle :
    Intention : We want to test the case 'plaintext' at line 124 of HTMLTreeBuilder (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HTMLTreeBuilderTest
    Utilité : Tester le comportement lors de l'ajout d'un plaintext tag
    Exécution : Via 'mvn mvn clean test -Dtest=HtmlTreeBuilderTest1 jacoco:report' ou Github Action
     */
     @Test
     public void initialiseParseFragment() {
        //ARRANGE
         // html fragment that tests the swich case for the case plaintext
         String fragment = "<plaintext>testing and stuff<div></div>";
 
         //Act
         // Parse only a fragment and not a full hmtl file
         Document doc = Jsoup.parseBodyFragment(fragment);
         Element body = doc.body();
         String parsed = body.text();
 
         // Assert
         assertEquals("testing and stuff<div></div>", parsed);
     }
 


    /*
    Test #2 : 
    Oracle :
    Intention : We want to test closeElement, to cover the method at line 818 of HTMLTreeBuilder (untested method) it implies feeding an html string with a <p> tag that was not closed so closeElement('p') can be called
    Structure : Arrange, Act, and Assert
    Doc : README -> HTMLTreeBuilderTest
    Utilité : Tester le comportement lorsqu'un tag <p> est oublié
    Exécution : Via 'mvn mvn clean test -Dtest=HtmlTreeBuilderTest1 jacoco:report' ou Github Action
     */
     @Test
     public void closeElement() {

        //ARRANGE
         // html fragment that tests the swich case for the case plaintext
        String html = "<html>\n" +
        "  <body>\n" +
        "    <p>open paragraph"+
        "    <form>\n" +
        "    </form>\n" +
        "  </body>\n" +
        "</html>";
         //Act
         // Parse the body with the form tag
         Document doc = Jsoup.parseBodyFragment(html);
         Element body = doc.body();
         String parsed = body.html();
         //System.out.println(parsed);
         // Assert
         String assertValue=
         "<p>open paragraph</p>\n"+
         "<form>\n"+
         "</form>";
         assertEquals(assertValue, parsed);
     }

    
}

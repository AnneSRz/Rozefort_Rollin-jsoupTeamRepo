package org.jsoup.Rozefort_Rollin_Tache2_test;

import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import static org.junit.jupiter.api.Assertions.*;


public class HtmlTreeBuilderTest1 {

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

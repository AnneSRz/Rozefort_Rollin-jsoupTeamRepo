package org.jsoup.Rozefort_Rollin_Tache2_test;

import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import static org.junit.jupiter.api.Assertions.*;


public class HtmlTreeBuilderStateTest1 {

    /*
    Test #3 : 
    Oracle :
    Intention : We want to test the case 'nobr' in method inBodyStartTag at line 448 of HtmlTreeBuilderState (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HtmlTreeBuilderState
    Utilité : Tester le comportement lors de l'ajout d'un nobr deprecated tag. Va fermer automatiquement les tags ouverts
    Exécution : Via 'mvn clean test -Dtest=HtmlTreeBuilderStateTest1 jacoco:report' ou Github Action
     */
    @Test
    public void inBodyStartTag() {

       //ARRANGE
        // html fragment that tests the swich case for the case plaintext
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

}
 

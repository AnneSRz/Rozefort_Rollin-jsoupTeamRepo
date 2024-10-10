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

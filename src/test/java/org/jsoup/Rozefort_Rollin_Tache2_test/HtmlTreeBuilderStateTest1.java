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
    public void inBodyStartTag1() {

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

    /*
    Test #4 : Bundle de plusieurs tests  
    Oracle :
    Intention : We want to test the case 'nobr' in method InColumnGroup at line 1207 of HtmlTreeBuilderState (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HtmlTreeBuilderState
    Utilité : Tester le comportement lors de l'ajout d'un nobr deprecated tag. Va fermer automatiquement les tags ouverts
    Exécution : Via ' mvn -Dtest=HtmlTreeBuilderStateTest1 test' ou Github Action
     */
    @Test
    public void inBodyStartTag2() {

       //ARRANGE
        // part 1) if invalid table, we delete
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
    public void inBodyStartTag3() {

       //ARRANGE
        // part 1) if invalid table, we delete
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
        // Assert
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
    Test #5 : 
    Oracle :
    Intention : We want to test the case 'nobr' in method InColumnGroup at line 1207 of HtmlTreeBuilderState (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HtmlTreeBuilderState
    Utilité : Tester le comportement lors de l'ajout d'un nobr deprecated tag. Va fermer automatiquement les tags ouverts
    Exécution : Via ' mvn -Dtest=HtmlTreeBuilderStateTest1 test' ou Github Action
     */
    @Test
    public void inColumnGroup() {

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



}
 

package org.jsoup.Rozefort_Rollin_Tache2_test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TokeniserStateTest1 {

    // Le cas ScriptDataDoubleEscapedDash et ScriptDataDoubleEscapedDashDash n'avait pas été testé

    // Dans ce test, on va allé vérifié un flux incluant des tirets consécutifs.

    // But : Vérifier la logique de tokenisation pour voir comment elle gère
    // les données de script contenant des séquences avec des doubles tirets et assurer que l'intégrité des balises de script sont maintenus.

    //Test #6
    @Test
    public void testForScriptDataDoubleEscapedDash() {

        // Puisque la chaine de caractère commenet par -- et un espace on devrait aller chercher ScriptDataDoubleEscapedDash
        String triggeringSnippet = "<div><-- testy testing --></div>";

        Document doc = Jsoup.parse(triggeringSnippet);
        Elements els = doc.select("div");

        assertEquals("&lt;-- testy testing --&gt;", els.html().trim());
    }

    // Test #7
    @Test
    public void testForScriptDataDoubleEscapedDashDash() {
        String triggeringSnippet = "<div>Testy testing -- avec tirets -- et <tags></tags></div>";
        
        Document doc = Jsoup.parse(triggeringSnippet);
        Elements els = doc.select("div");

        assertEquals("Testy testing -- avec tirets -- et <tags></tags>", els.html().trim());
    }

}

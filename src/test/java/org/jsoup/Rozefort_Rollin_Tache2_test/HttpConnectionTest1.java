package org.jsoup.Rozefort_Rollin_Tache2_test;

import org.junit.jupiter.api.Test;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;


public class HttpConnectionTest1 {

    //Test #8 (ligne 237 - 244)
    //Ici on veut s'assurer que la méthode permet bien d'enregistrée les valeurs pour qu'on puisse
    // par la suite y accéder.
    @Test
    public void dataMapTest() {
        
        //Arranger
        Map<String, String> dataValues = new LinkedHashMap<>();
        dataValues.put("test1", "val1");
        dataValues.put("test2", "val2");
        dataValues.put("test3", "val3");

        Connection con = HttpConnection.connect("http://example.com/");
        //Act
        //Ajouter les valeurs a l'objet connection 
        con.data(dataValues);

        // Assert
        Collection<Connection.KeyVal> values = con.request().data(); 

        Connection.KeyVal one = values.iterator().next();
        assertEquals("test1", one.key());
        assertEquals("val1", one.value());
    
    }

    //Test #9 (ligne 524 - 534)
    @Test
    public void HeaderMapTest() {
        
        // Arrangerà
        Map<String, String> headerValues = new LinkedHashMap<>();
        headerValues.put("header1", "SubjectTitle");
        headerValues.put("header2", "Quote");

        Connection con = HttpConnection.connect("http://example.com/");
        
        // Act 
        // Ajouter les valeurs à l'objet connection
        con.headers(headerValues);

        // Assert
        Map<String, String> getHeaders = con.request().headers();
        assertEquals("SubjectTitle", getHeaders.get("header1"));
        assertEquals("Quote", getHeaders.get("header2"));
    }

    //Test#10 (ligne 581 - 586)
    /*
    Oracle :
    Intention : We want to test the case 'DOCTYPE' in state InColumnGroup at line 1185 of HtmlTreeBuilderState (untested case)
    Structure : Arrange, Act, and Assert
    Doc : README -> HtmlTreeBuilderState
    Utilité : Tester le comportement lors de l'ajout d'un nested doctype tag. Va simplement supprimer le tag HTML, Doctype. Pour le tag illegal template, le parser le handle.
    Exécution : Via ' mvn -Dtest=HtmlTreeBuilderStateTest1 test' ou Github Action
    */
    @Test
    public void removeCookie() {
        //Arranger
        HttpConnection.Response req = new HttpConnection.Response();

        req.cookies().put("brownie", "choccy");
        req.cookies().put("biscoui", "vanilla");

        //Act
        req.removeCookie("brownie");

        //Assert
        //Le cookie a t'il bien été supprimé?
        assertFalse(req.cookies().containsKey("brownie"));
        //s'assurer que l'autre cookie est la
        assertTrue(req.cookies().containsKey("biscoui"));
        assertEquals("biscoui", req.cookies().get("vanilla"));
    }
}

package org.jsoup.Rozefort_Rollin_Tache2_test;

import org.junit.jupiter.api.Test;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;


public class HttpConnectionTest1 {

    //Test #6
    @Test
    public void createNewCookieStoreTest() {
        
        //Arranger
        Map<String, String> dataValues = new LinkedHashMap<>();
        dataValues.put("test1", "val1");
        dataValues.put("test2", "val2");
        dataValues.put("test3", "val3");

        Connection con = HttpConnection.connect("http://example.com/");
        //Act
        con.data(dataValues);

        // Assert
        Collection<Connection.KeyVal> values = con.request().data(); 

        Connection.KeyVal first = values.iterator().next();
        assertEquals("test1", first.key());
        assertEquals("val1", first.value());
    
    }
}

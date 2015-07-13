/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sebicom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sebicom.domain.City;
import com.sebicom.domain.Job;
import com.sebicom.util.Log;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mark
 */
public class QueryServiceTest {

    public QueryServiceTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of queryWithZipcode method, of class QueryService.
     */
    @Test
    public void testQueryWithZipcode() throws Exception {
        System.out.println("queryWithZipcode");
        String searchText = "software dev java";
        String zipcode = "11222";
        QueryService instance = new QueryService();
        Job[] results = instance.queryByZipcode(searchText, zipcode, 0, 10).getJob();
        assertNotNull(results);
        ObjectMapper mapper = new ObjectMapper();
        for (Job searchHit : results) {
            Log.d(QueryServiceTest.class, "\nFound job result: " + searchHit.toString());
        }
    }

    /**
     * Test of queryWithCityOrState method, of class QueryService.
     */
    @Test
    public void testQueryWithState() throws Exception {
        Log.i(QueryServiceTest.class, "queryWithState");
        String searchText = "nurse";
        //String state = "new york";
        String state = "New Jersey";
        QueryService instance = new QueryService();
        Job[] results = instance.queryByState(searchText, state, 0, 10).getJob();
        assertNotNull(results);
        ObjectMapper mapper = new ObjectMapper();
        for (Job searchHit : results) {
            Log.d(QueryServiceTest.class, "\nFound job result: " + searchHit.getTitle() + " " + searchHit.toString());
        }
    }

    /**
     * Test of queryRadiusByLatLon method, of class QueryService.
     */
    @Test
    public void testQueryRadius() {
        System.out.println("queryRadiusByLatLon");
        int radiusMiles = 5;
        String zipcode = "11788";
        String searchText = "nurse";

        LookupService lookupService = new LookupService();
        City city = null;
        try {
            city = lookupService.cityRecordLookupByZipcode(zipcode);
        } catch (IOException ex) {
            Logger.getLogger(QueryServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertNotNull(city);
        Double lat = city.getLocation().getLat();
        Double lon = city.getLocation().getLon();

        QueryService instance = new QueryService();
        Job[] results = null;

        results = instance.queryRadius(searchText, radiusMiles, lat, lon, 0, 10).getJob();

        assertNotNull(results);

        ObjectMapper mapper = new ObjectMapper();
        for (Job searchHit : results) {
            Log.d(QueryServiceTest.class, "\nFound job result: " + searchHit.getTitle() + " " + searchHit.toString());
        }
    }

    /**
     * Test of queryRadiusByGeoHash method, of class QueryService.
     */
    @Test
    public void testQueryRadiusByGeoHash() {
        System.out.println("queryRadiusByGeoHash");
        int radiusMiles = 0;
        String geoHash = "";
        QueryService instance = new QueryService();
        FilterBuilder expResult = null;
        FilterBuilder result = instance.queryRadiusByGeoHash(radiusMiles, geoHash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryRadiusByLatLon method, of class QueryService.
     */
    @Test
    public void testQueryRadiusByLatLon() {
        System.out.println("queryRadiusByLatLon");
        int radiusMiles = 0;
        Double lat = null;
        Double lon = null;
        QueryService instance = new QueryService();
        FilterBuilder expResult = null;
        FilterBuilder result = instance.queryRadiusByLatLon(radiusMiles, lat, lon);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryRadiusForResponse method, of class QueryService.
     */
    @Test
    public void testQueryRadiusForResponse() {
        System.out.println("queryRadiusForResponse");
        FilterBuilder locationFilter = null;
        QueryBuilder multiMatchQuery = null;
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryRadiusForResponse(locationFilter, multiMatchQuery, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryRadiusSearchQueryText method, of class QueryService.
     */
    @Test
    public void testQueryRadiusSearchQueryText() {
        System.out.println("queryRadiusSearchQueryText");
        String searchText = "";
        QueryService instance = new QueryService();
        QueryBuilder expResult = null;
        QueryBuilder result = instance.queryRadiusSearchQueryText(searchText);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryFilterByField method, of class QueryService.
     */
    @Test
    public void testQueryFilterByField() {
        System.out.println("queryFilterByField");
        String fieldName = "";
        String fieldValue = "";
        QueryService instance = new QueryService();
        FilterBuilder expResult = null;
        FilterBuilder result = instance.queryFilterByField(fieldName, fieldValue);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryRadius method, of class QueryService.
     */
    @Test
    public void testQueryRadius_4args() {
        System.out.println("queryRadius");
        String searchText = "";
        int radiusMiles = 0;
        Double lat = null;
        Double lon = null;
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryRadius(searchText, radiusMiles, lat, lon, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryByField method, of class QueryService.
     */
    @Test
    public void testQueryByField() {
        System.out.println("queryByField");
        String searchText = "";
        String fieldName = "";
        String fieldValue = "";
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryByField(searchText, fieldName, fieldValue, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryByCityAndState method, of class QueryService.
     */
    @Test
    public void testQueryByCityAndState() {
        System.out.println("queryByCityAndState");
        String searchText = "";
        String cityValue = "";
        String stateValue = "";
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryByCityAndState(searchText, cityValue, stateValue, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryByCityAndStateCode method, of class QueryService.
     */
    @Test
    public void testQueryByCityAndStateCode() {
        System.out.println("queryByCityAndStateCode");
        String searchText = "";
        String cityValue = "";
        String stateCodeValue = "";
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryByCityAndStateCode(searchText, cityValue, stateCodeValue, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryByState method, of class QueryService.
     */
    @Test
    public void testQueryByState() {
        System.out.println("queryByState");
        String searchText = "";
        String fieldValue = "";
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryByState(searchText, fieldValue, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryByStateCode method, of class QueryService.
     */
    @Test
    public void testQueryByStateCode() {
        System.out.println("queryByStateCode");
        String searchText = "nurse";
        String stateCode = "ny";
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] results = instance.queryByStateCode(searchText, stateCode, 0, 10).getJob();

        assertNotNull(results);

        ObjectMapper mapper = new ObjectMapper();
        Job jobResult = null;
        for (Job searchHit : results) {
            Log.i(QueryServiceTest.class, "\nFound job result: " + searchHit.getTitle() + " " + jobResult.toString());
        }
    }

    /**
     * Test of queryByZipcode method, of class QueryService.
     */
    @Test
    public void testQueryByZipcode() {
        System.out.println("queryByZipcode");
        String searchText = "";
        String zipcode = "";
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryByZipcode(searchText, zipcode, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of queryRadius method, of class QueryService.
     */
    @Test
    public void testQueryRadius_3args() {
        System.out.println("queryRadius");
        String searchText = "";
        int radiusMiles = 0;
        String geoHash = "";
        QueryService instance = new QueryService();
        SearchHit[] expResult = null;
        Job[] result = instance.queryRadius(searchText, radiusMiles, geoHash, 0, 10).getJob();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}

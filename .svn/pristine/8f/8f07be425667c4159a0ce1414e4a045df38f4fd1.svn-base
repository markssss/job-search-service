/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sebicom.service;

import com.sebicom.domain.City;
import com.sebicom.util.Log;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mark
 */
public class LookupServiceTest {

    public LookupServiceTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of zipcodeLookup method, of class LookupService.
     */
    @Test
    public void testZipcodeLookup() throws Exception {
        System.out.println("zipcodeLookup");
        String city = "Elmira";
        String stateCode = "NY";
        LookupService instance = new LookupService();
        String result = null;
        try {
            result = instance.zipcodeLookup(city, stateCode);
        } catch (IOException ex) {
            Log.e(LookupServiceTest.class, ex.toString());
        }
        Log.d(LookupServiceTest.class, result);
        assertNotNull(result);
    }

    /**
     * Test of cityRecordLookupByCityState method, of class LookupService.
     */
    @Test
    public void testCityRecordLookupByCityState() throws Exception {
        System.out.println("cityRecordLookupByCityState");
        String city = "";
        String stateCode = "";
        LookupService instance = new LookupService();
        City expResult = null;
        City result = instance.cityRecordLookupByCityState(city, stateCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cityRecordLookupByZipcode method, of class LookupService.
     */
    @Test
    public void testCityRecordLookupByZipcode() throws Exception {
        Log.d(LookupServiceTest.class, "cityRecordLookupByZipcode");
        String zipcode = "07002";
        LookupService instance = new LookupService();
        City result = instance.cityRecordLookupByZipcode(zipcode);
        assertNotNull(result);
        Log.d(LookupServiceTest.class, result.toString());
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sebicom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sebicom.domain.City;
import com.sebicom.util.Log;
import java.io.IOException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

/**
 *
 * @author mark
 */
public class LookupService {

    private TransportClient client;

    public LookupService() {
        client = TransportClientFactory.getInstance();
    }

    public String zipcodeLookup(String city, String stateCode) throws IOException {

        //Log.d(LookupService.class, "Finding zipcode: " + city + " stateCode: " + stateCode);

        String zipcode = null;

        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.matchQuery("city", city))
                .must(QueryBuilders.matchQuery("stateCode", stateCode));

        SearchResponse response = client.prepareSearch("jobz")
                .setTypes("cities")
                .setQuery(qb).execute().actionGet();

        if (response != null && response.getHits().getTotalHits() > 0) {
            SearchHits hit = response.getHits();
            //Log.d(LookupService.class, "Hit size: " + hit.totalHits());

            ObjectMapper mapper = new ObjectMapper();
            City cityNew = mapper.readValue(hit.getAt(0).sourceAsString(), City.class);
            zipcode = cityNew.getZipcode();
            //Log.e(LookupService.class, "Found zipcode: " + zipcode
            //        + " " + city + " " + stateCode);
        } else {
            Log.e(LookupService.class, "No Hits for: city: " + city + " stateCode: " + stateCode);
        }
        return zipcode;
    }

    public City cityRecordLookupByCityState(String city, String state) throws IOException {

        //Log.d(LookupService.class, "Finding zipcode: " + city + " state: " + state);
        City cityNew = null;
        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.matchQuery("city", city))
                .must(QueryBuilders.matchQuery("state", state));

        SearchResponse response = client.prepareSearch("jobz")
                .setTypes("cities")
                .setQuery(qb).execute().actionGet();

        if (response != null && response.getHits().getTotalHits() > 0) {
            SearchHits hit = response.getHits();
           // Log.d(LookupService.class, "Hit size: " + hit.totalHits());

            ObjectMapper mapper = new ObjectMapper();
            cityNew = mapper.readValue(hit.getAt(0).sourceAsString(), City.class);

        } else {
            Log.e(LookupService.class, "No Hits for: city: " + city + " state: " + state);
        }

        return cityNew;
    }

    public City cityRecordLookupByCityStateCode(String city, String stateCode) throws IOException {

        //Log.d(LookupService.class, "Finding zipcode: " + city + " stateCode: " + stateCode);
        City cityNew = null;
        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.matchQuery("city", city))
                .must(QueryBuilders.matchQuery("stateCode", stateCode));

        SearchResponse response = client.prepareSearch("jobz")
                .setTypes("cities")
                .setQuery(qb).execute().actionGet();

        if (response != null && response.getHits().getTotalHits() > 0) {
            SearchHits hit = response.getHits();
            //Log.d(LookupService.class, "Hit size: " + hit.totalHits());

            ObjectMapper mapper = new ObjectMapper();
            cityNew = mapper.readValue(hit.getAt(0).sourceAsString(), City.class);

        } else {
            Log.e(LookupService.class, "No Hits for: city: " + city + " stateCode: " + stateCode);
        }

        return cityNew;
    }

    public City cityRecordLookupByZipcode(String zipcode) throws IOException {

        //Log.d(LookupService.class, "Finding zipcode: " + zipcode);
        City cityNew = null;
        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.matchQuery("zipcode", zipcode));

        SearchResponse response = client.prepareSearch("jobz")
                .setTypes("cities")
                .setQuery(qb).execute().actionGet();

        if (response != null && response.getHits().getTotalHits() > 0) {
            SearchHits hit = response.getHits();
            //Log.d(LookupService.class, "Hit size: " + hit.totalHits());

            ObjectMapper mapper = new ObjectMapper();
            cityNew = mapper.readValue(hit.getAt(0).sourceAsString(), City.class);

        } else {
            Log.e(LookupService.class, "No Hits for: zipcode: " + zipcode);
        }

        return cityNew;
    }

}

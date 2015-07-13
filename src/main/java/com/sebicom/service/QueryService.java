/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sebicom.service;

import com.sebicom.domain.City;
import com.sebicom.domain.JobsResponse;
import com.sebicom.util.ConvertSearchHitToJob;
import com.sebicom.util.Log;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author mark
 */
public class QueryService {

    private final static String FIELD_STATE = "state";
    private final static String FIELD_STATE_CODE = "stateCode";
    private final static String FIELD_ZIPCODE = "zipcode";
    private TransportClient client;
    private LookupService lookupService;
    private int radius = 10;

    public QueryService() {
        client = TransportClientFactory.getInstance();
        lookupService = new LookupService();
    }

    protected FilterBuilder queryRadiusByLatLon(int radiusMiles, Double lat, Double lon) {
        return FilterBuilders.geoDistanceFilter("location")
                .point(lat, lon)
                .distance(radiusMiles, DistanceUnit.MILES);
    }

    protected FilterBuilder queryRadiusByGeoHash(int radiusMiles, String geoHash) {
        return FilterBuilders.geoDistanceFilter("geo_filter_" + radiusMiles + "mi")
                .geohash(geoHash)
                .distance(radiusMiles, DistanceUnit.MILES);
    }

    protected JobsResponse queryRadiusForResponse(FilterBuilder locationFilter, QueryBuilder multiMatchQuery, int from, int size) {
        JobsResponse jobsResponse = new JobsResponse();
        QueryBuilder filteredQuery = QueryBuilders.filteredQuery(multiMatchQuery, locationFilter);
        SearchResponse response = client.prepareSearch("jobz")
                .setTypes("jobs")
                .setQuery(filteredQuery)
                .setFrom(from)
                .setSize(size)
                //.setPostFilter(FilterBuilders.scriptFi)
                .execute().actionGet();

        if (response != null && response.getHits().getTotalHits() > 0) {
            jobsResponse.setJob(ConvertSearchHitToJob.convert(response.getHits().getHits()));
            jobsResponse.setTotalHits(response.getHits().getTotalHits());
            Log.d(QueryService.class, "Hit size: " + jobsResponse.getJob().length);

        } else {
            Log.e(QueryService.class, "No Hits.");
        }
        return jobsResponse;
    }

    protected QueryBuilder queryRadiusSearchQueryText(String searchText) {
       // return QueryBuilders.multiMatchQuery(searchText, "title^10", "summary", "description", "keywords");
        return QueryBuilders.disMaxQuery()
                            .add(QueryBuilders.multiMatchQuery(searchText, "title^10", "summary", "description", "keywords").boost(10f))
                            .add(QueryBuilders.fuzzyLikeThisQuery("title", "summary", "description", "keywords").likeText(searchText).fuzziness(Fuzziness.AUTO));
    }

    protected FilterBuilder queryFilterByField(String fieldName, String fieldValue) {
        return FilterBuilders.queryFilter(QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery(fieldName, fieldValue)));
    }

    public JobsResponse queryRadius(String searchText, int radiusMiles, Double lat, Double lon, int from, int size) {

        Log.d(QueryService.class, "Using search string: " + searchText + " lat: " + lat + " lon: " + lon);

        QueryBuilder disMaxQuery = queryRadiusSearchQueryText(searchText);
        FilterBuilder locationFilter = queryRadiusByLatLon(radiusMiles, lat, lon);

        return queryRadiusForResponse(locationFilter, disMaxQuery, from, size);
    }

    public JobsResponse queryByField(String searchText, String fieldName, String fieldValue, int from, int size) {
        Log.d(QueryService.class, "Using search string: " + searchText + " " + fieldName + ": " + fieldValue);
        JobsResponse jobsResponse = new JobsResponse();
        QueryBuilder qb = QueryBuilders.filteredQuery(
                    QueryBuilders.disMaxQuery()
                            .add(QueryBuilders.multiMatchQuery(searchText, "title^10", "summary", "description", "keywords").boost(10f))
                            .add(QueryBuilders.fuzzyLikeThisQuery("title", "summary", "description", "keywords").likeText(searchText).fuzziness(Fuzziness.AUTO)),
                queryFilterByField(fieldName, fieldValue));
        
       // QueryBuilder qb = QueryBuilders.filteredQuery(QueryBuilders.multiMatchQuery(searchText, "title^10", "summary", "description", "keywords"),
        //        queryFilterByField(fieldName, fieldValue));
        
        SearchResponse response = client.prepareSearch("jobz")
                .setTypes("jobs")
                .setQuery(qb)
                .setFrom(from)
                .setSize(size)
                .execute().actionGet();

        if (response != null && response.getHits().getTotalHits() > 0) {
            jobsResponse.setJob(ConvertSearchHitToJob.convert(response.getHits().getHits()));
            jobsResponse.setTotalHits(response.getHits().getTotalHits());
            Log.d(QueryService.class, "Hit size: " + jobsResponse.getJob().length);

        } else {

            Log.e(QueryService.class, "No Hits for: searchText: " + searchText + " " + fieldName + ": " + fieldValue);
        }
        return jobsResponse;
    }

    public JobsResponse queryByCityAndState(String searchText, String cityValue, String stateValue, int from, int to) {
        City city = null;
        try {
            city = lookupService.cityRecordLookupByCityState(cityValue, stateValue);
        } catch (Throwable ex) {
            Log.e(QueryService.class, ex.toString() + " Failed looking up city: " + cityValue + " state: " + stateValue);
        }
        if (city == null) {
            Log.e(QueryService.class, " queryByCityAndState() - Failed looking up city: " + cityValue + " state: " + stateValue);
            return new JobsResponse();
        }

        return queryRadius(searchText, radius, city.getLocation().getLat(), city.getLocation().getLon(), from, to);
    }

    public JobsResponse queryByCityAndStateCode(String searchText, String cityValue, String stateCodeValue, int from, int size) {
        City city = null;
        try {
            city = lookupService.cityRecordLookupByCityStateCode(cityValue, stateCodeValue);
        } catch (Throwable ex) {
            Log.e(QueryService.class, ex.toString() + " Failed looking up city: " + cityValue + " stateCodeValue: " + stateCodeValue);
        }
        if (city == null) {
            Log.e(QueryService.class, " queryByCityAndState() - Failed looking up city: " + cityValue + " stateCodeValue: " + stateCodeValue);
            return null;
        }

        return queryRadius(searchText, radius, city.getLocation().getLat(), city.getLocation().getLon(), from, size);
    }

    public JobsResponse queryByState(String searchText, String fieldValue, int from, int size) {
        return queryByField(searchText, FIELD_STATE, fieldValue, from, size);
    }

    public JobsResponse queryByStateCode(String searchText, String fieldValue, int from, int size) {
        return queryByField(searchText, FIELD_STATE_CODE, fieldValue, from, size);
    }

    public JobsResponse queryByZipcode(String searchText, String zipcode, int from, int size) {
        City city = null;
        try {
            city = lookupService.cityRecordLookupByZipcode(zipcode);
        } catch (Throwable ex) {
            Log.e(QueryService.class, ex.toString() + " Failed looking up zipcode: " + zipcode);
        }
        if (city == null) {
            Log.e(QueryService.class, " queryByZipcode() - Failed looking up zipcode: " + zipcode);
            return null;
        }

        return queryRadius(searchText, radius, city.getLocation().getLat(), city.getLocation().getLon(), from, size);
    }

    public JobsResponse queryRadius(String searchText, int radiusMiles, String geoHash, int from, int size) {
        Log.d(QueryService.class, "Using search string: " + searchText + " geohash: " + geoHash);

        QueryBuilder disMaxQuery = queryRadiusSearchQueryText(searchText);
        FilterBuilder locationFilter = queryRadiusByGeoHash(radiusMiles, geoHash);

        return queryRadiusForResponse(locationFilter, disMaxQuery, from, size);
    }

//    public SearchHit[] queryWithState(String searchText, String state) throws IOException {
//
//        Log.d(QueryService.class, "Using search string: " + searchText + " state: " + state);
//        SearchHit[] hits = null;
//        QueryBuilder qb = QueryBuilders.filteredQuery(QueryBuilders.multiMatchQuery(searchText, "title^10", "summary", "description", "keywords"),
//                FilterBuilders.queryFilter(QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("state", state))));
//        //FilterBuilders.queryFilter(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("state", state))));
//        SearchResponse response = client.prepareSearch("jobz")
//                .setTypes("jobs")
//                .setQuery(qb)
//                // .setPostFilter(FilterBuilders.termFilter("state", state))
//                .execute().actionGet();
//
//        if (response != null && response.getHits().getTotalHits() > 0) {
//            hits = response.getHits().getHits();
//            Log.d(QueryService.class, "Hit size: " + hits.length);
//
//        } else {
//            Log.e(QueryService.class, "No Hits for: searchText: " + searchText + " state: " + state);
//        }
//        return hits;
//    }
//    public SearchHit[] queryWithStateCode(String searchText, String stateCode) {
//
//        Log.d(QueryService.class, "Using search string: " + searchText + " state: " + stateCode);
//        SearchHit[] hits = null;
//        QueryBuilder qb = QueryBuilders.filteredQuery(QueryBuilders.multiMatchQuery(searchText, "title^10", "summary", "description", "keywords"),
//                queryFilterByField("stateCode", stateCode));
//        //FilterBuilders.queryFilter(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("state", state))));
//        SearchResponse response = client.prepareSearch("jobz")
//                .setTypes("jobs")
//                .setQuery(qb)
//                // .setPostFilter(FilterBuilders.termFilter("state", state))
//                .execute().actionGet();
//
//        if (response != null && response.getHits().getTotalHits() > 0) {
//            hits = response.getHits().getHits();
//            Log.d(QueryService.class, "Hit size: " + hits.length);
//
//        } else {
//            Log.e(QueryService.class, "No Hits for: searchText: " + searchText + " state: " + state);
//        }
//        return hits;
//    }    
//    public SearchHit[] queryWithZipcode(String searchText, String zipcode) throws IOException {
//
//        Log.d(LookupService.class, "Using search string: " + searchText + " zipcode: " + zipcode);
//        SearchHit[] hits = new SearchHit[]{};
//        QueryBuilder qb = QueryBuilders
//                .boolQuery()
//                .should(QueryBuilders.multiMatchQuery(searchText, "title", "summary", "description", "keywords").field("title", .3f));
//        //.must(QueryBuilders.matchQuery("zipcode", zipcode));
//
//        SearchResponse response = client.prepareSearch("jobz")
//                .setTypes("jobs")
//                .setQuery(qb)
//                // .setPostFilter(FilterBuilders.inFilter("zipcode", zipcode))
//                .setPostFilter(FilterBuilders.inFilter("zipcode", zipcode))
//                .execute().actionGet();
//
//        if (response != null && response.getHits().getTotalHits() > 0) {
//            hits = response.getHits().getHits();
//            Log.d(QueryService.class, "Hit size: " + hits.length);
//
//        } else {
//            Log.e(QueryService.class, "No Hits for: searchText: " + searchText + " zipcode: " + zipcode);
//        }
//        return hits;
//    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sebicom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sebicom.domain.Job;
import com.sebicom.util.Log;
import java.util.List;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;

/**
 *
 * @author mark
 */
public class IndexService {

    private TransportClient client;

    public IndexService() {
        client = TransportClientFactory.getInstance();
    }

    /**
     *
     * @param job
     * @param uniqueJobID
     * @param jobStoreID @see com.sebicom.domain.JobStores
     * @return
     * @throws JsonProcessingException
     */
    public boolean indexJob(Job job, String uniqueJobID, String jobStoreID) throws JsonProcessingException {
        boolean allIndexedSuccessfully = true;

        IndexRequestBuilder requestBuilder = client.prepareIndex("jobz", "jobs", uniqueJobID + "_" + jobStoreID);
        IndexResponse response = null;

        requestBuilder.setSource(new ObjectMapper().writeValueAsString(job));
        response = requestBuilder.execute()
                .actionGet();
        if (response.isCreated()) {
            // Log.d(TAG.getClass(), "Index document successfully inserted: " + citee.getCity() + "\n");
        } else {
            allIndexedSuccessfully = false;
            Log.e(IndexService.class, "\nIndex document FAILED: _ID: " + uniqueJobID + "_" + jobStoreID + "\n" + job.toString() + "\n");
        }

        return allIndexedSuccessfully;
    }

    /**
     *
     * @param jobList
     * @return
     */
    public boolean indexJobList(List<Job> jobList) {
        boolean allIndexedSuccessfully = true;
        int count = 0;
        IndexRequestBuilder requestBuilder = null;

        IndexResponse response = null;
        for (Job job : jobList) {
            try {
                requestBuilder = client.prepareIndex("jobz", "jobs", job.getId() + "_" + job.getJobStoreId());
                requestBuilder.setSource(new ObjectMapper().writeValueAsString(job));
                response = requestBuilder.execute()
                        .actionGet();
                if (response.isCreated()) {
                    count++;
                    // Log.d(TAG.getClass(), "Index document successfully inserted: " + citee.getCity() + "\n");
                } else {
                    allIndexedSuccessfully = false;
                    Log
                            .d(IndexService.class, "Index document FAILED: " + job.toJobString());
                }

            } catch (JsonProcessingException ex) {
                Log.e(IndexService.class, ex.toString());
            } catch (Throwable t) {
                Log.e(IndexService.class, t.toString());
            }
        }
        Log.d(IndexService.class, "Indexed " + count + " out of " + jobList.size());
        return allIndexedSuccessfully;
    }
}

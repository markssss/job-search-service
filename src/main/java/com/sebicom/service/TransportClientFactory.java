/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sebicom.service;

import com.sebicom.util.Log;
import com.sebicom.util.Properties;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 *
 * @author mark
 */
public class TransportClientFactory {
    private static String TAG = "ESClientService";
    private static TransportClient client;

    private TransportClientFactory() {
    }

    public static TransportClient getInstance(String propertiesFileName) {
        Log.d(TransportClient.class, "Getting instance: " + propertiesFileName);
        if (client == null) {
            createInstance(propertiesFileName);
        }
        return client;
    }

    public static TransportClient getInstance() {
        Log.d(TransportClient.class, "Getting default instance. ");
        if (client == null) {
            createInstance(null);
        }
        return client;
    }

    private static void createInstance(String propertiesFileName) {
        
        Properties properties = null;
        if (propertiesFileName == null) {
            Log.d(TransportClient.class, "Creating instance: Using default properties file - esr-client.properties");
            properties = new Properties("esr-client.properties");
        } else {
            Log.d(TransportClient.class, "Creating instance: Using supplied properties file - " + propertiesFileName);
            properties = new Properties(propertiesFileName);
        }

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", properties.getString("cluster.name"))
                .put("client.transport.sniff",
                        properties.getBoolean("client.transport.sniff"))
                .build();
        client = new TransportClient(settings);
        for (String ip : properties.getStringArray("cluster.socket.ip.addresses")) {
            client.addTransportAddress(new InetSocketTransportAddress(ip, 9300));
        }

        ImmutableList<DiscoveryNode> nodes = client.connectedNodes();
        if (nodes.isEmpty()) {
            throw new RuntimeException("No nodes available. Verify ES is running!");
        } else {
            Log.d(TransportClient.class, "Client connected to nodes: " + nodes.toString());
        }
    }

}

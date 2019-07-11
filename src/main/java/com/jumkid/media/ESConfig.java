package com.jumkid.media;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created at 17 Sep, 2018$
 *
 * @author chooliyip
 **/
@Configuration
public class ESConfig {

    private static final Logger logger = LoggerFactory.getLogger(ESConfig.class);

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port1}")
    private int esPort1;

    @Value("${elasticsearch.port2}")
    private int esPort2;

    @Value("${elasticsearch.cluster.name}")
    private String esClusterName;

    @Bean
    public RestHighLevelClient esClient(){

//        Settings esSettings = Settings.builder()
//                .put("cluster.name", esClusterName)
//                .build();

        try {
            return new RestHighLevelClient(RestClient.builder(
                    new HttpHost(InetAddress.getByName(esHost), esPort1, "http"),
                    new HttpHost(InetAddress.getByName(esHost), esPort2, "http")
                    ));
        } catch (UnknownHostException uhe) {
            logger.error("Failed to hock up to elasticsearch host {} ", esHost);
            return null;
        }

    }

}

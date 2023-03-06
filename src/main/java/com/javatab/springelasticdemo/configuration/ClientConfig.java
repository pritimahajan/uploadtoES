package com.javatab.springelasticdemo.configuration;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ClientConfig {

    @Value("${elastic.hosts}")
    private String esHost;
    
    @Value("${elastic.port}")
    private Integer esPort;

    @Value("${elastic.username}")
    private String username;
    
    @Value("${elastic.password}")
    private String password;

    @Bean
    public RestHighLevelClient client() {
        String[] esHosts = esHost.split(",");
        HttpHost[] oHosts = new HttpHost[esHosts.length];
        for (int i=0;i<esHosts.length; i++) {
            String[] hostProto = esHosts[i].split("://");
            oHosts[i] = new HttpHost(hostProto[1], esPort, hostProto[0]);
        }
        RestClientBuilder builder = RestClient.builder(oHosts);

        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider));
        return new RestHighLevelClient(builder);  
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

package com.chis.trugarden.shared.config;

import com.chis.trugarden.shared.properties.PayphoneProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @Qualifier("payphoneRestClient")
    public RestClient payphoneRestClient(PayphoneProperties props) {
        return RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader("Authorization", props.getToken())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

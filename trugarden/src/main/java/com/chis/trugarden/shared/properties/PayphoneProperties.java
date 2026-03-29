package com.chis.trugarden.shared.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payphone")
@Data
public class PayphoneProperties {
    private String token;
    private String baseUrl;
    private String storeId;
    private String callbackUrl;
    private String cancelUrl;
}

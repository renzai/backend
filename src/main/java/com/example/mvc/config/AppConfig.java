package com.example.mvc.config;

import com.example.mvc.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${github.token:}")
    private String oauthToken;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    HttpEntity<String> defaultHeaders() {
        if (StringUtils.isBlank(oauthToken)) {
            return new HttpEntity<>(null);
        }
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "token " + oauthToken);

        return new HttpEntity<>(headers);
    }

}

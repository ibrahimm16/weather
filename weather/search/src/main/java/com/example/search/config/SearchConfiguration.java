package com.example.search.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableSwagger2
public class SearchConfiguration {

    public static final String idEndpoint = "http://weather-details-service/details?city=";
    public static final String weatherEndpoint = "http://weather-details-service/details/";

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5)).build();
    }

    @Bean
    public ExecutorService getExecutorService() {
        return Executors.newCachedThreadPool();
    }
}

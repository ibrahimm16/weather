package com.example.gateway.service;

import com.example.gateway.config.GatewayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GatewayService implements GatewayServiceInterface {

    private final RestTemplate restTemplate;

    @Autowired
    public GatewayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Object getCitiesWeather(List<String> queryNames) {
        return restTemplate.getForObject(GatewayConfig.searchEndpoint + String.join(",", queryNames), Object.class);
    }
}

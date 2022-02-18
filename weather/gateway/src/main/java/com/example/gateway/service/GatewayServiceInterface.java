package com.example.gateway.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GatewayServiceInterface {

    Object getCitiesWeather(List<String> queryNames);
}

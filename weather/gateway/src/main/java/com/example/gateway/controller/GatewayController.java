package com.example.gateway.controller;

import com.example.gateway.service.GatewayService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GatewayController {

    private final GatewayService gatewayService;

    @Autowired
    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/weather")
    @HystrixCommand(fallbackMethod = "getFallbackWeather", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
    })
    public ResponseEntity<?> getWeather(@RequestParam List<String> cities) {
        return new ResponseEntity<>(gatewayService.getCitiesWeather(cities), HttpStatus.OK);
    }

    public ResponseEntity<?> getFallbackWeather(@RequestParam List<String> cities) {
        return new ResponseEntity<>("Error while processing request", HttpStatus.NOT_FOUND);
    }
}

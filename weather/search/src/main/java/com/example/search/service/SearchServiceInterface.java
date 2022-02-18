package com.example.search.service;

import com.example.search.pojo.City;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchServiceInterface {
    List<City> getCitiesWeather(List<String> queryNames);
}

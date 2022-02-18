package com.example.search.service;

import com.example.search.config.SearchConfiguration;
import com.example.search.pojo.City;
import com.example.search.pojo.CityIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class SearchService implements SearchServiceInterface {

    private final RestTemplate restTemplate;
    private final ExecutorService executorService;

    @Autowired
    public SearchService(RestTemplate restTemplate, ExecutorService executorService) {
        this.restTemplate = restTemplate;
        this.executorService = executorService;
    }

    @Override
    public List<City> getCitiesWeather(List<String> queryNames) {
        List<CompletableFuture<List<City>>> resultsFuture = new ArrayList<>();

        queryNames.forEach((queryName) -> {
            String idURL = SearchConfiguration.idEndpoint + queryName;

            CompletableFuture<List<City>> citiesListFuture = CompletableFuture.supplyAsync(() -> {
                CityIds queryIds = restTemplate.getForObject(idURL, CityIds.class);

                List<CompletableFuture<City>> citiesFuture = new ArrayList<>();
                queryIds.getData().forEach((id) -> {
                    String weatherEndpoint = SearchConfiguration.weatherEndpoint + id;
                    CompletableFuture<City> cityFuture = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(weatherEndpoint, City.class), executorService);
                    citiesFuture.add(cityFuture);
                });

                return CompletableFuture
                        .allOf(citiesFuture.toArray(new CompletableFuture[0]))
                        .thenApply(Void -> {
                            List<City> cities = new ArrayList<>();
                            citiesFuture.forEach((cityFuture) -> {
                                cities.add(cityFuture.join());
                            });
                            return cities;
                        }).join();
            }, executorService);

            resultsFuture.add(citiesListFuture);
        });

        return CompletableFuture.allOf(resultsFuture.toArray(new CompletableFuture[0])).thenApply(Void -> {
            List<City> cities = new ArrayList<>();
            resultsFuture.forEach((resultFuture) -> {
                cities.addAll(resultFuture.join());
            });
            return cities;
        }).join();
    }
}

package com.hotzin.wda.controller;

import com.hotzin.wda.client.GeoCodeClient;
import com.hotzin.wda.client.model.GeoCodeModel;
import com.hotzin.wda.client.model.GeoCodeModelWrapper;
import com.hotzin.wda.controller.model.WeatherResponse;
import com.hotzin.wda.model.ClientRawModel;
import com.hotzin.wda.service.CitiesService;
import com.hotzin.wda.service.CitiesToWSUrlsMappingService;
import com.hotzin.wda.service.WeatherDataAquirerService;
import com.hotzin.wda.service.WeatherDataMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherDataAquirerService weatherDataAquirerService;
    private final WeatherDataMappingService weatherDataMappingService;
    private final CitiesToWSUrlsMappingService citiesToWSUrlsMappingService;
    private final GeoCodeClient geoCodeClient;
    private final CitiesService citiesService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/")
    public HttpEntity<ClientRawModel> weatherEndpoint(@RequestParam String cityName){

        String weatherStationURI = citiesToWSUrlsMappingService.mapCityNameToWSUrl(cityName);
        String weatherData = weatherDataAquirerService.getWeatherData(URI.create(weatherStationURI));
        ClientRawModel clientRawModel = weatherDataMappingService.mapData(weatherData);

        GeoCodeModel geoCodeModel = geoCodeClient.mapToLocation(clientRawModel.getLatitude(), clientRawModel.getLongitude());

        return new HttpEntity<ClientRawModel>(clientRawModel);
                //HttpEntity<WeatherResponse>(WeatherResponse.constructResponse(geoCodeModel, clientRawModel));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/weather/city")
    public List<String> getCitiesNames(){

        return citiesService.returnAllCities();
    }

}

package com.hotzin.wda.client;

import com.hotzin.wda.client.model.GeoCodeModels;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class GeoCodeClient {

    //?q=LAT+LNG&key=

    private final RestTemplate restTemplate;
    private final String MAIN_URL = "https://api.opencagedata.com/geocode/v1/json";
    private final String access_key = System.getenv("open_cage_access_key");

    public GeoCodeModels mapToLocation(String latitude, String longitude){



        String uriString = UriComponentsBuilder.fromHttpUrl(MAIN_URL)
                .queryParam("key", access_key)
                .queryParam("q", latitude + "," + checkSing(longitude))
                .build()
                .toUriString();

        ResponseEntity<GeoCodeModels> response = restTemplate
                .getForEntity(uriString, GeoCodeModels.class);

        return response.getBody();

    }

    private String checkSing(String longitude){

        double dlongitude = Double.parseDouble(longitude);
        dlongitude *= -1;
        return Double.toString(dlongitude);
        //because (- for EAST of GMT) for davis stations wxdisplay
    }


}

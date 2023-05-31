package com.pruchnik.SolarObjects.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class HttpController{

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/aaa")
    public ResponseEntity<String> getNasaData(){
        String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=j76K9rCqfycLJm77sALgnqm4ZZNLmbUSO4Ot7EHz";
        String response = restTemplate.getForObject(apiUrl, String.class);

        return ResponseEntity.ok(response);
    }
}

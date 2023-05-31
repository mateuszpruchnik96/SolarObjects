package com.pruchnik.SolarObjects.simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class NasaApiScheduler {

    private static final Logger log = LoggerFactory.getLogger(NasaApiScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final String NasaApiKey = "j76K9rCqfycLJm77sALgnqm4ZZNLmbUSO4Ot7EHz";

    @Autowired
    private RestTemplate restTemplate;

    public NasaApiScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 12 * * ?") // every day at 12 PM
    public void sendRequestToNasaApi() {
        String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=" + NasaApiKey;
        String response = restTemplate.getForObject(apiUrl, String.class);
        // process the response as needed
    }

    @Scheduled(fixedRate = 1000*60)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
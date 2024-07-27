package org.flow.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@RestController
public class TestController {

    @Value("${auth.uri}")
    private String uri;

    @PostConstruct
    public void init() {
        System.out.println("uri : " + uri);
    }

    @CrossOrigin
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(uri, String.class);
    }

}

package com.example.currencyexchanger.feign;

import feign.RequestLine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.Map;

@org.springframework.cloud.openfeign.FeignClient(name = "currencyRateTrackerClient")
public interface FeignClient {

    @GetMapping
    @RequestLine("GET")
    ResponseEntity<Map<?, ?>> get(URI baseUri);

}

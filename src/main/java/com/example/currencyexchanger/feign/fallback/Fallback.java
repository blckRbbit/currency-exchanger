package com.example.currencyexchanger.feign.fallback;

import com.example.currencyexchanger.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
public class Fallback implements FeignClient {

    @Override
    public ResponseEntity<Map<?, ?>> get(URI baseUri) {
        return null;
    }

}

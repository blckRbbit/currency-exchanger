package com.example.currencyexchanger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example.feign.feign",
        "com.example.feign.controller"})
public class CurrencyExchangerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangerApplication.class, args);
    }
}

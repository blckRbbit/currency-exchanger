package com.example.currencyexchanger.controller;

import com.example.currencyexchanger.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    private final ExchangeRateService rateService;
    @Value("${currency.reference}")
    private String currencyReference;

    @Value("${currency.tracking}")
    private String currencyTracking;

    @Autowired
    public MainController(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public String start() {
        return "redirect:" + rateService.get();
    }

    @GetMapping(path = "/{currency}")
    public String get(@PathVariable String currency) {
        return "redirect:" + rateService.get(currency);
    }

    @GetMapping("/currencies")
    ResponseEntity<String> getCurrenciesCode() {
        StringBuilder currencies = new StringBuilder(
                "You can find out about changes in the " + currencyReference + " exchange rate against the following currencies: \n");
        for (String currency : rateService.getCurrencies()) {
            currencies.append(currency).append(", ");
        }
        String response = currencies.toString();
        response = response.substring(0, response.length() - 2);
        return ResponseEntity.ok(response);
    }
}
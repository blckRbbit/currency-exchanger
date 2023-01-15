package com.example.currencyexchanger.service;

import com.example.currencyexchanger.feign.FeignClient;
import feign.Feign;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Service
@Import(FeignClientsConfiguration.class)
public class ExchangeRateService {
    private final FeignClient client;
    private final PictureService service;

    @Value("${currency.tracking}")
    private String currencyTracking;

    @Value("${currency.reference}")
    private String currencyReference;

    @Value("${rate.api.url}")
    private String baseUrl;

    @Value("${openexchangerates.id}")
    private String apiId;

    @Autowired
    public ExchangeRateService(Encoder encoder, Decoder decoder, PictureService service) {
        this.service = service;
        this.client = Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .target(Target.EmptyTarget.create(FeignClient.class));
    }

    public String get(String ... args) {
        String link;
        if (exchangeRateIsRose(args)) {
            link = service.getRandomLink("wealth");
        } else  {
            link = service.getRandomLink("poor");
        }
        return link;
    }

    private boolean exchangeRateIsRose(String ... args) {
        boolean result;
        if (args.length > 0) {
            result = getTodayRate(args[0]) >= getYesterdayRate(args[0]);
        } else {
            result = getTodayRate() >= getYesterdayRate();
        }
        return result;
    }

    private double getTodayRate() {
        double reference = getTodayCurrencyRates().get(currencyReference);
        double tracking = getTodayCurrencyRates().get(currencyTracking);
        return  tracking/reference;
    }

    private double getTodayRate(String currencyTracking) {
        double reference = getTodayCurrencyRates().get(currencyReference);
        double tracking = getTodayCurrencyRates().get(currencyTracking);
        return tracking/reference;
    }

    private double getYesterdayRate() {
        double reference = getYesterdayCurrencyRates().get(currencyReference);
        double tracking = getYesterdayCurrencyRates().get(currencyTracking);
        return tracking/reference;
    }

    private double getYesterdayRate(String currencyTracking) {
        double reference = getYesterdayCurrencyRates().get(currencyReference);
        double tracking = getYesterdayCurrencyRates().get(currencyTracking);
        return tracking/reference;
    }

    public Set<String> getCurrencies() {
        return getTodayCurrencyRates().keySet();
    }

    private Map<String, Double> getTodayCurrencyRates() {
        String today = String.valueOf(LocalDate.now());
        URI uri;
        String path = String.format("%s%s.json?app_id=%s", baseUrl, today, apiId);
        try {
            uri = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return (Map<String, Double>) client.get(uri).getBody().get("rates");
    }

    private Map<String, Double> getYesterdayCurrencyRates() {
        String yesterday = String.valueOf(LocalDate.now().minusDays(1));
        URI uri;
        String path = String.format("%s%s.json?app_id=%s", baseUrl, yesterday, apiId);
        try {
            uri = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return (Map<String, Double>) client.get(uri).getBody().get("rates");
    }
}


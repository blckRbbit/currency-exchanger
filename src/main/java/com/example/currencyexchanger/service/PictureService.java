package com.example.currencyexchanger.service;

import com.example.currencyexchanger.feign.FeignClient;
import feign.Feign;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Import(FeignClientsConfiguration.class)
public class PictureService {

    private final FeignClient client;

    @Value("${pictures.api.url}")
    private String baseUrl;

    @Value("${pixabay.id}")
    private String apiId;

    @Autowired
    public PictureService(Encoder encoder, Decoder decoder) {
        this.client = Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .target(Target.EmptyTarget.create(FeignClient.class));
    }

    public String getRandomLink(String key) {
        Random random = new Random();
        List<String> links = getLinks(key);
        int index = random.nextInt(links.size());
        return links.get(index);
    }

    private List<String> getLinks(String key) {
        JSONArray pictures = (JSONArray) Objects.requireNonNull(getJson(key)).get("hits");
        Iterator<?> iterator = pictures.iterator();
        List<String> links = new ArrayList<>();
        while (iterator.hasNext()) {
            JSONObject inner = (JSONObject) iterator.next();
            String link = (String) inner.get("webformatURL");
            links.add(link);
        }
        return links;
    }

    private JSONObject getJson(String key) {
        String temp = JSONObject.toJSONString(getApiAnswer(key));
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(temp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    private Map<String, String> getApiAnswer(String key) {
        URI uri;
        String path = String.format("%s?key=%s&q=%s", baseUrl, apiId, key);
        try {
            uri = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return (Map<String, String>) client.get(uri).getBody();
    }
}

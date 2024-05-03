package com.justinwu.lineaichatbot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.justinwu.lineaichatbot.model.cwa.WeatherData;
import com.justinwu.lineaichatbot.model.cwa.WeatherJsonConverter;
import com.justinwu.lineaichatbot.service.CWAService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CWAServiceImpl implements CWAService {

    @Value("${cwa.api.token}")
    private String token;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getWeather() throws JsonProcessingException {
        String url = "https://opendata.cwa.gov.tw/api/v1/rest/datastore/F-D0047-065";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("Authorization", token)
                .queryParam("locationName", "三民區")
                .queryParam("elementName", "T,Wx,PoP3h")
                .queryParam("timeFrom", "2024-05-04T00:00:00")
                .queryParam("timeTo", "2024-05-05T23:59:59");

        URI finalUri = builder.build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(finalUri, HttpMethod.GET, entity, String.class);

        WeatherJsonConverter weatherJsonConverter = new WeatherJsonConverter();
        List<WeatherData> weatherDataList = weatherJsonConverter.convert(response.getBody());


        Map<String, List<String>> groupedWeather = new LinkedHashMap<>();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for(WeatherData weatherData : weatherDataList){
            LocalDateTime dateTime = LocalDateTime.parse(weatherData.getDataTime(), inputFormatter);
            String timeRange = timeFormatter.format(dateTime) + "-" + timeFormatter.format(dateTime.plusHours(3));

            String temperature = weatherData.getT().getValue() + "℃";
            String description = weatherData.getWx().getValue();

            String summary = "\n時間 : " + timeRange + "\n溫度 : " + temperature + "\n天氣 : " + description + "\n";
            String dayKey = dateTime.toLocalDate().toString();

            groupedWeather.computeIfAbsent(dayKey, k -> new ArrayList<>()).add(summary);
        }

        // 使用 StringBuilder 來構建最終輸出
        StringBuilder answer = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : groupedWeather.entrySet()) {
            answer.append("-----------------------\n");
            answer.append("日期 : ").append(entry.getKey()).append("\n");
            for (String data : entry.getValue()) {
                answer.append(data);
            }
        }

        return answer.toString();
    }
}

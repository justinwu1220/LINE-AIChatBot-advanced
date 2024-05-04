package com.justinwu.lineaichatbot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.justinwu.lineaichatbot.model.cwa.WeatherData;
import com.justinwu.lineaichatbot.model.cwa.WeatherElement;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public String getWeather(Integer searchDays) throws JsonProcessingException {
        // 使用 LocalDate.now() 取得今日的日期
        LocalDate startDay = LocalDate.now();

        // 使用 LocalTime.of() 設定時間為 00:00:00
        LocalTime startTime = LocalTime.of(0, 0, 0);
        LocalTime endTime = LocalTime.of(23, 59, 59);

        // 組合日期和時間
        LocalDateTime startDayMidnight = LocalDateTime.of(startDay, startTime);
        LocalDateTime endDayMidnight = LocalDateTime.of(startDay.plusDays(searchDays-1), endTime);


        // 定義所需的格式，cwa api 要求格式為 "yyyy-MM-dd'T'HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // 格式化日期時間對象為字符串
        String formattedStartDay = startDayMidnight.format(formatter);
        String formattedEndDay = endDayMidnight.format(formatter);

        System.out.println("start : " + formattedStartDay);
        System.out.println("end : " + formattedEndDay);

        String url = "https://opendata.cwa.gov.tw/api/v1/rest/datastore/F-D0047-065";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("Authorization", token)
                .queryParam("locationName", "三民區")
                .queryParam("elementName", "T,Wx,PoP3h")
                .queryParam("timeFrom", formattedStartDay)
                .queryParam("timeTo", formattedEndDay);

        URI finalUri = builder.build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(finalUri, HttpMethod.GET, entity, String.class);

        WeatherJsonConverter weatherJsonConverter = new WeatherJsonConverter();
        WeatherData weatherData = weatherJsonConverter.convert(response.getBody());

        String resultText = simplifyWeatherData(weatherData);

        return resultText;
    }

    private String simplifyWeatherData(WeatherData weatherData){
        Map<String, List<String>> groupedWeather = new LinkedHashMap<>();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<WeatherElement> weatherElementList = weatherData.getWeatherElementList();
        for(WeatherElement weatherElement : weatherElementList){
            LocalDateTime dateTime = LocalDateTime.parse(weatherElement.getDataTime(), inputFormatter);
            String timeRange = timeFormatter.format(dateTime) + "-" + timeFormatter.format(dateTime.plusHours(3));

            String temperature = weatherElement.getT().getValue() + "℃";
            String description = weatherElement.getWx().getValue();

            String summary = "\n" + timeRange + "\n" + temperature + "\n" + description + "\n";
            String dayKey = dateTime.toLocalDate().toString();

            groupedWeather.computeIfAbsent(dayKey, k -> new ArrayList<>()).add(summary);
        }

        // 使用 StringBuilder 來構建最終輸出
        StringBuilder textData = new StringBuilder();
        textData.append(weatherData.getCity() + "  " + weatherData.getDistrict() + '\n');
        for (Map.Entry<String, List<String>> entry : groupedWeather.entrySet()) {
            textData.append("----------------\n");
            textData.append(entry.getKey()).append("\n");
            for (String data : entry.getValue()) {
                textData.append(data);
            }
        }

        return textData.toString();
    }
}

package com.justinwu.lineaichatbot.service.impl;

import com.justinwu.lineaichatbot.service.OpenAIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openAi.api.url}")
    private String apiUrl;

    @Value("${openAi.api.key}")
    private String apiKey ;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getChatGPTResponse(String prompt) {

        //建立傳給openAI post 的 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        //建立json request body 內含使用模型、溝通指令prompt、最大token數
        String requestBody = "{\"model\":\"gpt-3.5-turbo-instruct\",\"prompt\":\"" + prompt + "\",\"max_tokens\":100}";

        //整合Header和requestBody 成完整的 http request
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        //將http request 用 post 方式 傳給openAI api，並取得回應
        var response = restTemplate.postForEntity(apiUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            //回傳回應的body
            return response.getBody();
        } else {
            return "Error";
        }
    }
}

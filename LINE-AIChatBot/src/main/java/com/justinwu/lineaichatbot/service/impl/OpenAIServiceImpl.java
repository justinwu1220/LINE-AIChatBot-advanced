package com.justinwu.lineaichatbot.service.impl;

import com.justinwu.lineaichatbot.model.AiResponse;
import com.justinwu.lineaichatbot.model.Choice;
import com.justinwu.lineaichatbot.service.OpenAIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.util.List;


@Component
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openAi.api.url}")
    private String apiUrl;

    @Value("${openAi.api.key}")
    private String apiKey ;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getChatGPTResponse(String messageText) {

        //建立傳給openAI post 的 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        //建立json request body 內含使用模型、使用的角色role、溝通指令content
        //String requestBody = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{ \"role\": \"user\", \"content\": \""+ messageText +"\" }] }";
        String requestBody = String.format(
                "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{ \"role\": \"user\", \"content\": \"%s\" }] }", messageText);

        //整合Header和requestBody 成完整的 http request
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        //將http request 用 post 方式 傳給openAI api，並取得回應
        var response = restTemplate.postForEntity(apiUrl, request, String.class);

        AiResponse aiResponse = parseJson(response.getBody());
        List<Choice> choices = aiResponse.getChoices();
        StringBuilder answer = new StringBuilder();
        for(Choice choice : choices){
            String content = choice.getMessage().getContent();
            answer.append(content);
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            //回傳回應的body
            return answer.toString();
        } else {
            return "Error";
        }
    }

    @Override
    public AiResponse parseJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 解析JSON為Java object
            AiResponse result = objectMapper.readValue(json, AiResponse.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

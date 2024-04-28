package com.justinwu.lineaichatbot.service;

import com.justinwu.lineaichatbot.model.AiResponse;

public interface OpenAIService {
    String getChatGPTResponse(String prompt);
    AiResponse parseJson(String json);
}

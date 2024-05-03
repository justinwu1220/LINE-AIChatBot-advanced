package com.justinwu.lineaichatbot.service;

import com.justinwu.lineaichatbot.model.openAI.AiResponse;

public interface OpenAIService {
    String getChatGPTResponse(String messageText);
}

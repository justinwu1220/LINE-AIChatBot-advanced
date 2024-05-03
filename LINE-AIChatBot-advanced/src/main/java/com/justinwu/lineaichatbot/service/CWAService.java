package com.justinwu.lineaichatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CWAService {
    String getWeather() throws JsonProcessingException;
}

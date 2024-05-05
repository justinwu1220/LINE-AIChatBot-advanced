package com.justinwu.lineaichatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.justinwu.lineaichatbot.model.user.User;

public interface CWAService {
    String getWeather(User user, Integer searchDays) throws JsonProcessingException;
}

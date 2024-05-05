package com.justinwu.lineaichatbot.service;

import com.justinwu.lineaichatbot.model.user.User;

public interface UserService {
    User getUser(String userId);

    User createUser(String userId);
    String settingLocation(String userId, String locationText);
}

package com.justinwu.lineaichatbot.dao;

import com.justinwu.lineaichatbot.model.user.User;

public interface UserDao {
    User getUserById(String userId);
    void createUser(String userId);
    void updateUserLocation(String userId, String city, String district);
}

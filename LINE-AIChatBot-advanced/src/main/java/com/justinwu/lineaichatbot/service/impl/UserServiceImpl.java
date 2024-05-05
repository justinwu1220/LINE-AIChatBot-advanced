package com.justinwu.lineaichatbot.service.impl;

import com.justinwu.lineaichatbot.dao.UserDao;
import com.justinwu.lineaichatbot.model.user.User;
import com.justinwu.lineaichatbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public User getUser(String userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User createUser(String userId) {
        userDao.createUser(userId);

        return userDao.getUserById(userId);
    }

    @Override
    public String settingLocation(String userId, String locationText) {

        String[] location = locationText.split(" ");

        userDao.updateUserLocation(userId, location[1], location[2]);

        String city = userDao.getUserById(userId).getCity();
        String district = userDao.getUserById(userId).getDistrict();

        return "已" + location[0] + ": " + city + " " + district;
    }
}

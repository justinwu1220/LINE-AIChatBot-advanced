package com.justinwu.lineaichatbot.dao.impl;

import com.justinwu.lineaichatbot.dao.UserDao;
import com.justinwu.lineaichatbot.model.user.User;
import com.justinwu.lineaichatbot.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public User getUserById(String userId) {
        String sql = "SELECT user_id, city, district " +
                "FROM user WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        //namedParameterJdbcTemplate.query會返回list
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        //userList若為空，則返回null
        if(userList.size()>0) {
            return userList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public void createUser(String userId) {
        String sql = "INSERT INTO user(user_id, city, district)" +
                "VALUES (:userId, :city, :district)";

        //用map將變數值帶入sql中
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        //預設城市
        map.put("city", "臺北市");
        map.put("district", "中正區");

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void updateUserLocation(String userId, String city, String district) {
        String sql = "UPDATE user SET city = :city, district = :district " +
                "WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("city", city);
        map.put("district", district);

        namedParameterJdbcTemplate.update(sql, map);
    }
}

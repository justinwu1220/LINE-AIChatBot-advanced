package com.justinwu.lineaichatbot.rowmapper;

import com.justinwu.lineaichatbot.model.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setCity(rs.getString("city"));
        user.setDistrict(rs.getString("district"));

        return user;
    }
}

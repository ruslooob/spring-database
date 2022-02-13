package com.ruslooob.jdbctemplate.repository.mapper;

import com.ruslooob.jdbctemplate.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("user_id");
        String name = rs.getString("user_name");
        String email = rs.getString("user_email");

        return new User(id, name, email);
    }

}

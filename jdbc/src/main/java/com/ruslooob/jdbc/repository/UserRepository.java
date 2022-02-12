package com.ruslooob.jdbc.repository;

import com.ruslooob.jdbc.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    public List<User> getAll() throws SQLException {
        try(Connection connection = ConnectionResolver.getConnection()) {
            //language=sql
            String sql = """
                select id AS user_id,
                       name AS user_name,
                       email AS user_email
                from users;
                """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                Long userId = resultSet.getLong("user_id");
                String userName = resultSet.getString("user_name");
                String userEmail = resultSet.getString("user_email");

                users.add(new User(userId, userName, userEmail));
            }

            return users;
        }
    }

    public User getUser(Long id) throws SQLException {
        try(Connection connection = ConnectionResolver.getConnection()) {
            //language=sql
            String sql = """
                select id AS user_id,
                       name AS user_name,
                       email AS user_email
                from users
                where users.id = ?;
                """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next() && resultSet.getLong("user_id") != 0) {
                Long userId = resultSet.getLong("user_id");
                String userName = resultSet.getString("user_name");
                String userEmail = resultSet.getString("user_email");

                return new User(userId, userName, userEmail);
            } else {
                return null;
            }
        }
    }

    public User saveUser(User user) throws SQLException {
        try(Connection connection = ConnectionResolver.getConnection()) {
                //language=sql
            String sql = """
                    insert into users(name, email)
                        values(?, ?);
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());

            stmt.executeUpdate();


            ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();

            user.setId(generatedKeys.getLong(1));

            return user;
        }
    }

//    public User updateUser() throws SQLException {
//
//    }
//
//    public  void deleteUser() throws SQLException {
//
//    }

}

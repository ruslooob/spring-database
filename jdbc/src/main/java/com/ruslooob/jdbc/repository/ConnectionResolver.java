package com.ruslooob.jdbc.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ConnectionResolver {

    private static String user;
    private static String password;
    private static String url;

    @Autowired
    public ConnectionResolver(Environment env) {
        user = env.getProperty("spring.datasource.username");
        password = env.getProperty("spring.datasource.password");
        url = env.getProperty("spring.datasource.url");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}

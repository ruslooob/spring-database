package com.ruslooob.jdbc;

import com.ruslooob.jdbc.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JdbcApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void contextLoads() {
        assertNotNull(userRepository);
    }

}

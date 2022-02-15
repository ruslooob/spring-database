package com.ruslooob.jdbctemplate.repository;

import com.ruslooob.jdbctemplate.model.User;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    static int MAX_BATCH_SIZE = 100_000;
    static int MEDIUM_BATCH_SIZE = 30_000;
    static int SMALL_BATCH_SIZE = 1000;

    static User testUser =  new User(1L, "Ruslan", "rm952576@gmail.com");

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Test
    void getUser() {
        User dbUser = userRepository.findById(1L).get();
        int countRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        assertNotNull(dbUser);
        assertEquals(1L, dbUser.getId());
        assertEquals("Ruslan", dbUser.getName());
        assertEquals("rm952576@gmail.com", dbUser.getEmail());
        assertEquals(countRows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    void saveUser() {
        int countRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        User dbUser = userRepository.save(new User("Saved User", "savedEmail@gmail.com"));

        assertNotNull(dbUser);
        assertEquals(++countRows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    void updateUser() {
        int countRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        User updatedUser = new User(1L, "Ruslan2", "rmasdkfadsk;fj@gmail.com");
        User dbUser = userRepository.update(updatedUser);


        assertEquals(countRows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
        assertEquals(dbUser, updatedUser);
    }

    @Test
    void deleteUser() {
        int countRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        userRepository.deleteById(testUser.getId());

        assertEquals(countRows - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    static List<Long> userIds = new ArrayList<>(MEDIUM_BATCH_SIZE);
    static List<User> users = new ArrayList<>(MEDIUM_BATCH_SIZE);
    static {
        for (int i = 0; i < MEDIUM_BATCH_SIZE; i++) {
            userIds.add((long) i);
            users.add(new User("user" + i, "user" + i + "@gmail.com"));
        }
    }

    @Test
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    void butchGetWithFor() {
        saveAll();

         for (int i = 0; i < MEDIUM_BATCH_SIZE; i++) {
             userRepository.findById((long) i);
         }
    }

    @Test
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    // breaking down, in BATCH_SIZE = 100_000 (m.b. exceeded MEDIUMimum request length)
    // on batch_size == 40_000 its breaks too
    // on 30_000 all ok
    // in this benchmark you should subtract time to saveAll method,
    // because getAll method use their (~400ms on 30_000 elems)
    void getAll() {
        saveAll();

        userRepository.findAll(userIds);
    }

    @Test
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    void butchSaveWithFor() {
        for (int i = 0; i < MEDIUM_BATCH_SIZE; i++) {
            userRepository.save(users.get(i));
        }
    }

    @Test
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    void saveAll() {
        userRepository.saveAll(users);
    }

}
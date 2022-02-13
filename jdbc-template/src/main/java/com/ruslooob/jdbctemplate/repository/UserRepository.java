package com.ruslooob.jdbctemplate.repository;

import com.ruslooob.jdbctemplate.model.User;
import com.ruslooob.jdbctemplate.repository.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sql = """
                SELECT id AS user_id,
                     name AS user_name,
                     email AS user_email
                FROM users;
                """;

        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public Optional<User> findById(Long id) {
        String sql = """
                SELECT id AS user_id,
                     name AS user_name,
                     email AS user_email
                FROM users
                WHERE users.id = ?;
                """;

        // if all query aliases match with fields name,
        // you can pass BeanPropertyRowMapper instead you own mapper
        // because its generate trivial mapper for you, and you do not write it.
        // in this example I have specially given other aliases,
        // because in 95% cases it will be like this.
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new UserRowMapper(), id));
    }

    public User save(User user) {
        String sql = """
                INSERT INTO users(name, email)
                        VALUES (?, ?);
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    return ps;
                },
                keyHolder);

        Long generatedId = (long) requireNonNull(keyHolder.getKeys()).get("id");
        user.setId(generatedId);

        return user;
    }

    public User update(User user) {
        String sql = """
                UPDATE users
                SET name = ?,
                    email = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getId());

        return findById(user.getId()).orElseThrow(() -> new RuntimeException("can not find user after save"));
    }

    public void deleteById(Long id) {
        String sql = """
                DELETE FROM users
                WHERE users.id = ?
                """;

        jdbcTemplate.update(sql, id);
    }



}

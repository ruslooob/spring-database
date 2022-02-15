package com.ruslooob.jdbctemplate.repository;

import com.ruslooob.jdbctemplate.model.User;
import com.ruslooob.jdbctemplate.repository.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
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
                FROM users
                """;

        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public List<User> findAll(List<Long> ids) {
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));

        String sql = """
                SELECT id AS user_id,
                     name AS user_name,
                     email AS user_email
                FROM users
                WHERE id IN(%s)
                """;

        return jdbcTemplate.query(format(sql, inSql),
                new UserRowMapper(),
                ids.toArray());
    }

    public Optional<User> findById(Long id) {
        String sql = """
                SELECT id AS user_id,
                     name AS user_name,
                     email AS user_email
                FROM users
                WHERE users.id = ?
                """;

        // if all query aliases match with fields name,
        // you can pass BeanPropertyRowMapper instead you own mapper
        // because its generate trivial mapper for you, and you do not write it.
        // in this example I have specially given other aliases,
        // because in 95% cases it will be like this.
        return jdbcTemplate.query(sql, new UserRowMapper(), id).stream().findAny();
    }

    public User save(User user) {
        String sql = """
                INSERT INTO users(name, email)
                        VALUES (?, ?)
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

    public void saveAll(List<User> users) {
        String sql = """
                INSERT INTO users(name, email)
                        VALUES (?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, users.get(i).getName());
                ps.setString(2, users.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
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

    public List<User> updateAll(List<User> users) {
        String sql = """
                UPDATE users
                SET name = ?,
                    email = ?
                WHERE id = ?
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, users.get(i).getName());
                ps.setString(2, users.get(i).getEmail());
                ps.setLong(3, users.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });

        List<Long> userIds = users.stream().map(User::getId).toList();

        return findAll(userIds);
    }

    public void deleteById(Long id) {
        String sql = """
                DELETE FROM users
                WHERE users.id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    public void deleteAll(List<Long> ids) {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

}

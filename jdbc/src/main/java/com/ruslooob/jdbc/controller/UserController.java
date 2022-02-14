package com.ruslooob.jdbc.controller;

import com.ruslooob.jdbc.model.User;
import com.ruslooob.jdbc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAll() throws SQLException {
        return ResponseEntity.ok(userRepository.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) throws SQLException {
        return ResponseEntity.ok(userRepository.getUser(id));
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) throws SQLException  {
        return ResponseEntity.ok(userRepository.saveUser(user));
    }

    @PutMapping
    ResponseEntity<User> updateUser(@RequestBody User user) throws SQLException {
        return ResponseEntity.ok(userRepository.saveUser(user));
    }

    @DeleteMapping
    ResponseEntity<Void> deleteUser(@RequestBody User user) throws SQLException {
        userRepository.deleteUser(user);
        return ResponseEntity.ok().build();
    }
}

package com.ruslooob.jpa.controller;

import com.ruslooob.jpa.model.User;
import com.ruslooob.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

}

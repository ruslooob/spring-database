package com.ruslooob.jdbctemplate.controller;

import com.ruslooob.jdbctemplate.model.User;
import com.ruslooob.jdbctemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    // todo make another repository with jdbc template and inject it here by interface
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/batch")
    public ResponseEntity<List<User>> getAll(@RequestParam Long[] ids) {
        return ResponseEntity.ok(userService.findAll(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> saveAll(@RequestBody List<User> users) {
        userService.saveAll(users);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @PutMapping("/batch")
    public ResponseEntity<List<User>> updateAll(@RequestBody List<User> users) {
        return ResponseEntity.ok(userService.updateAll(users));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteAll(@RequestParam Long[] ids) {
        userService.deleteAll(ids);
        return ResponseEntity.ok().build();
    }
}

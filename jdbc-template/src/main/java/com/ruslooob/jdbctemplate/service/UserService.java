package com.ruslooob.jdbctemplate.service;

import com.ruslooob.jdbctemplate.model.User;
import com.ruslooob.jdbctemplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.stream;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAll(Long[] userIds) {
        return userRepository.findAll(stream(userIds).toList());
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("can not find user with id " + id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    public User update(User user) {
        return userRepository.update(user);
    }

    public List<User> updateAll(List<User> users) {
        return userRepository.updateAll(users);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAll(Long[] ids) {
        userRepository.deleteAll(stream(ids).toList());
    }

}

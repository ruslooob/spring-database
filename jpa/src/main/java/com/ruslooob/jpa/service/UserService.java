package com.ruslooob.jpa.service;

import com.ruslooob.jpa.exception.UserAlreadyExistException;
import com.ruslooob.jpa.model.User;
import com.ruslooob.jpa.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.log = log;
    }

    public List<User> findAllUsers() {
//        log.info("get all users");

        return userRepository.findAll();
    }

    public User getUser(Long id) {
        User userById = userRepository.getById(id);
//        log.info(String.format("get user %s", userById));

        return userById;
    }

    public User saveUser(User user) {
//        log.info(String.format("find user %s", user));
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
//            log.warn(String.format("user %s already exist", user));
            throw new UserAlreadyExistException(user);
        }

//        log.info(String.format("user %s saved to database", user));

        return userRepository.save(user);
    }

    public User updateUser(User user) {
//        log.info(String.format("update user %s", user));

        return userRepository.save(user);
    }

    public void deleteUser(User user) {
//        log.info(String.format("delete user %s", user));
        userRepository.delete(user);
    }

}

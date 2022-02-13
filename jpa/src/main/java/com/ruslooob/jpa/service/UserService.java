package com.ruslooob.jpa.service;

import com.ruslooob.jpa.exception.UserAlreadyExistException;
import com.ruslooob.jpa.exception.UserNotFoundException;
import com.ruslooob.jpa.model.User;
import com.ruslooob.jpa.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        logger.info("get all users");

        return userRepository.findAll();
    }

    public User getUser(Long id) {
        Optional<User> userById = userRepository.findById(id);
        logger.info(String.format("get user %s", userById));

        return userById.orElseThrow(() -> new UserNotFoundException(id));
    }

    public User saveUser(User user) {
        logger.info(String.format("find user %s", user));
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
            logger.warn(String.format("user %s already exist", user));
            throw new UserAlreadyExistException(user);
        }

        logger.info(String.format("user %s saved to database", user));

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        logger.info(String.format("update user %s", user));

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        logger.info(String.format("delete user with id %d", id));
        userRepository.deleteById(id);
    }

}

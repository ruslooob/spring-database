package com.ruslooob.jpa.service;

import com.ruslooob.jpa.exception.UserAlreadyExistException;
import com.ruslooob.jpa.exception.UserNotFoundException;
import com.ruslooob.jpa.model.User;
import com.ruslooob.jpa.repository.UserRepository;
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
    }

    public List<User> findAllUsers() {

        return userRepository.findAll();
    }

    public User getUser(Long id) {
        Optional<User> userById = userRepository.findById(id);

        return userById.orElseThrow(() -> new UserNotFoundException(id));
    }

    public User saveUser(User user) {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
            throw new UserAlreadyExistException(user);
        }

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}

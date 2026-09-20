package com.One.YourSenior.service;

import com.One.YourSenior.model.User;
import java.util.List;

public interface UserService {
    User registerUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    User validateCredentials(String email, String password);
}
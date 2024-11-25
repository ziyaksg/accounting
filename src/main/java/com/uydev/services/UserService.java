package com.uydev.services;

import com.uydev.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    void createNewUser(UserDTO newUser);

    UserDTO getUserByUserName(String username);

    UserDTO getUserById(Long userId);

    void updateUser(UserDTO updatedUser);

    void delete(Long userId);
}

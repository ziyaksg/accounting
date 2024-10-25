package com.uydev.services;
import com.uydev.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    void createNewUser(UserDTO newUser);
}

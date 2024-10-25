package com.uydev.services.impl;

import com.uydev.dto.UserDTO;
import com.uydev.entity.User;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.UserRepository;
import com.uydev.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MapperUtil mapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = repository.findAll();
        return userList.stream().map(user->mapper.convert(user, new UserDTO())).toList();

    }

    @Override
    public void createNewUser(UserDTO newUser) {
        repository.save(mapper.convert(newUser, new User()));
    }
}

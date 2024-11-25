package com.uydev.services.impl;

import com.uydev.dto.UserDTO;
import com.uydev.entity.User;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.UserRepository;
import com.uydev.services.SecurityService;
import com.uydev.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MapperUtil mapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    @Override
    public List<UserDTO> getAllUsers() {
        UserDTO currentUser = securityService.getLoggedInUser();

        var companyTitle = currentUser.getCompany().getTitle();

        List<User> userList = repository.findAllAndIsDeleted(false);

        if (currentUser.getId() == 1) {
            return userList.stream().filter(user -> user.getId() != 1 && user.getRole().getDescription().equals("Admin")).
                    map(user -> mapper.convert(user, new UserDTO())).toList();
        }
        return userList.stream().filter(user -> user.getId() != 1 && user.getCompany().getTitle().equals(companyTitle)).
                map(user -> mapper.convert(user, new UserDTO())).toList();

    }

    @Override
    public void createNewUser(UserDTO newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        repository.save(mapper.convert(newUser, new User()));
    }

    @Override
    public UserDTO getUserByUserName(String username) {
        return mapper.convert(repository.findUserByUsernameAndIsDeleted(username, false), new UserDTO());
    }

    @Override
    public UserDTO getUserById(Long userId) {

        return mapper.convert(repository.findById(userId), new UserDTO());
    }

    @Override
    public void updateUser(UserDTO updatedUser) {
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        repository.save(mapper.convert(updatedUser, new User()));
    }

    @Override
    public void delete(Long userId) {
        User user = repository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("there is No user with id " + userId));
        user.setIsDeleted(true);
        repository.save(user);
    }
}

package com.uydev.services.impl;

import com.uydev.dto.UserDTO;
import com.uydev.entity.User;
import com.uydev.entity.common.CustomUserDetails;
import com.uydev.mapper.MapperUtil;
import com.uydev.services.SecurityService;
import com.uydev.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserService userService;
    private final MapperUtil mapper;

    public SecurityServiceImpl(@Lazy UserService userService, MapperUtil mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDto = userService.getUserByUserName(username);
        if (userDto == null) {
            throw new UsernameNotFoundException("No User with this user name");
        }
        return new CustomUserDetails(mapper.convert(userDto, new User()));
    }

    @Override
    public UserDTO getLoggedInUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUserName(username);
    }
}

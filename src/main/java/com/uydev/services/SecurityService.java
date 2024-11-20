package com.uydev.services;

import com.uydev.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {


    UserDTO getLoggedInUser();
}

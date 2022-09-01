package com.cloudova.service.user.services;

import com.cloudova.service.user.models.User;
import com.cloudova.service.user.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findByMobileOrEmail(username,username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findByMobileOrEmail(username,username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
}


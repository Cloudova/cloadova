package com.cloudova.service.user.services;

import com.cloudova.service.user.models.Role;
import com.cloudova.service.user.models.User;
import com.cloudova.service.user.models.UserDto;
import com.cloudova.service.user.repositories.UserRepository;
import com.cloudova.service.user.services.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final OTPService otpService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, OTPService otpService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findByMobileOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public User createUser(String code, String identifier, UserDto userDto) {
        this.otpService.verifyOTP(identifier, code);
        User.UserBuilder builder = User.builder().role(Role.USER);
        if (userDto.email() != null) {
            builder = builder.email(identifier);
        } else {
            builder = builder.mobile(identifier);
        }
        User user = builder.firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .password(this.passwordEncoder.encode(userDto.password())).build();
        return this.repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.findUserByEmailOrMobile(username);
    }

    public User findUserByEmailOrMobile(String mobileOrEmail) throws UsernameNotFoundException {
        return this.repository.findByMobileOrEmail(mobileOrEmail, mobileOrEmail).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public boolean validateCredentials(UserDetails user, String password) {
        return this.passwordEncoder.matches(password, user.getPassword());
    }

}


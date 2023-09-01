package com.soroosh.auth.user.services;

import com.soroosh.auth.user.models.Role;
import com.soroosh.auth.user.models.User;
import com.soroosh.auth.user.models.UserDto;
import com.soroosh.auth.user.repositories.UserRepository;
import com.soroosh.auth.user.services.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User findById(long id) throws UsernameNotFoundException {
        return this.repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public User createUserWithOtp(String code, String identifier, UserDto userDto) {
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

    public User createUser(UserDto userDto) {
        User.UserBuilder builder = User.builder().role(Role.USER);
        User user = builder.firstName(userDto.firstName())
                .email(userDto.email())
                .mobile(userDto.mobile())
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

    public Optional<User> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    public Optional<User> findByMobile(String mobile) {
        return this.repository.findByMobile(mobile);
    }


    public boolean validateCredentials(UserDetails user, String password) {
        return this.passwordEncoder.matches(password, user.getPassword());
    }

}


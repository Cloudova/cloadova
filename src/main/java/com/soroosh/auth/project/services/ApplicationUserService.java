package com.soroosh.auth.project.services;

import com.soroosh.auth.jwt.services.JWTService;
import com.soroosh.auth.project.dto.ApplicationUserDto;
import com.soroosh.auth.project.models.Application;
import com.soroosh.auth.project.models.ApplicationUser;
import com.soroosh.auth.project.repositories.ApplicationUserRepository;
import com.soroosh.auth.user.services.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("application_user_auth")
public class ApplicationUserService {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final OTPService otpService;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, OTPService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }

    public ApplicationUser createUser(String code, String identifier, Application application, ApplicationUserDto userDto) {
        this.otpService.verifyOTP(identifier, code, application.getId());
        ApplicationUser.ApplicationUserBuilder user = ApplicationUser.builder()
                .application(application)
                .name(userDto.getName())
                .email(identifier)
                .username(userDto.getUsername())
                .password(this.passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getMetadata() != null) {
            user = user.metadata(userDto.getMetadata().stream().map(ApplicationUserDto.MetaDataDto::toMetaData).collect(Collectors.toList()));
        }
        return this.userRepository.save(user.build());
    }

    public ApplicationUser loadByUsernameOrEmail(String usernameOrEmail, String applicationSubdomain) {
        return this.userRepository.findByUsernameOrEmail(usernameOrEmail, applicationSubdomain)
                .orElseThrow(() -> new BadCredentialsException("Credentials are not match with our records"));
    }

    public String authenticate(ApplicationUser user, String password) throws AuthenticationException {
        if (!user.isBanned() && user.isActive() && this.passwordEncoder.matches(password, user.getPassword())) {
            return this.jwtService.generateTokenForApplicationUser(user);
        }
        throw new BadCredentialsException("Credentials are not match with our records");
    }

}

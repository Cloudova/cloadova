package com.soroosh.auth.user.http.controllers;

import com.soroosh.auth.commons.http.HttpStatusResponse;
import com.soroosh.auth.jwt.services.JWTService;
import com.soroosh.auth.user.http.requests.CreateUserRequest;
import com.soroosh.auth.user.http.requests.LoginRequest;
import com.soroosh.auth.user.http.requests.SendOtpRequest;
import com.soroosh.auth.user.http.resources.OtpSent;
import com.soroosh.auth.user.models.User;
import com.soroosh.auth.user.services.UserService;
import com.soroosh.auth.user.services.otp.OTPService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    private final OTPService otpService;
    private final UserService userService;
    private final JWTService jWTService;

    @Autowired
    public AuthenticationController(OTPService otpService, UserService userService, JWTService jWTService) {
        this.otpService = otpService;
        this.userService = userService;
        this.jWTService = jWTService;
    }

    @PostMapping("/otp/request")
    public OtpSent requestOtp(@Validated @RequestBody SendOtpRequest request) {
        UUID verificationID = this.otpService.sendOtp(request.identifier());
        return new OtpSent(true, verificationID.toString());
    }

    @PostMapping("/register")
    public HttpStatusResponse<User> register(@Validated @RequestBody CreateUserRequest request) {
        return new HttpStatusResponse<>(true, this.userService.createUser(request.code(), request.identifier(), request.user()));
    }

    @PostMapping("/login")
    public HttpStatusResponse<String> login(@RequestBody LoginRequest request) {
        boolean status = false;
        User userDetails = null;
        try {
            userDetails = this.userService.findUserByEmailOrMobile(request.email());
            status = this.userService.validateCredentials(userDetails, request.password());
        }catch (UsernameNotFoundException ex){
            // Nothing
        }
        return new HttpStatusResponse<>(status, null, status ? this.jWTService.generateToken(userDetails) : null);
    }

    @PostMapping("/logout")
    public HttpStatusResponse<String> logout(@RequestHeader("Authorization") String authorization) {
        this.jWTService.invokeToken(authorization.replace("Bearer ", ""));
        return new HttpStatusResponse<>(true);
    }

}

package com.cloudova.service.user.http.controllers;

import com.cloudova.service.commons.http.HttpStatusResponse;
import com.cloudova.service.jwt.services.JWTService;
import com.cloudova.service.user.http.requests.CreateUserRequest;
import com.cloudova.service.user.http.requests.LoginRequest;
import com.cloudova.service.user.http.requests.SendOtpRequest;
import com.cloudova.service.user.http.requests.VerifyOtpRequest;
import com.cloudova.service.user.http.resources.OtpSent;
import com.cloudova.service.user.models.User;
import com.cloudova.service.user.services.UserService;
import com.cloudova.service.user.services.otp.OTPService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
        UUID verificationID = this.otpService.SendOtp(request.identifier());
        return new OtpSent(true, verificationID.toString());
    }

    @PostMapping("/register")
    public HttpStatusResponse<User> register(@Validated @RequestBody CreateUserRequest request) {
        return new HttpStatusResponse<>(true, this.userService.createUser(request.code(), request.identifier(), request.user()));
    }

    @PostMapping("/login")
    public HttpStatusResponse<String> login(@RequestBody LoginRequest request) {
        User userDetails = this.userService.findUserByEmailOrMobile(request.email());
        boolean status = this.userService.validateCredentials(userDetails, request.password());
        return new HttpStatusResponse<>(status, status ? this.jWTService.generateToken(userDetails) : null);
    }

    @PostMapping("/logout")
    public HttpStatusResponse<String> logout(@RequestHeader("Authorization") String authorization) {
        this.jWTService.invokeToken(authorization.replace("Bearer ", ""));
        return new HttpStatusResponse<>(true);
    }

    @PostMapping("/otp/verify")
    public HttpStatusResponse<String> verifyOtp(@Validated @RequestBody VerifyOtpRequest request) {
        this.otpService.verifyOTP(request.identifier(), request.code());
        return new HttpStatusResponse<>(true, null);
    }


}

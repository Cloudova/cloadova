package com.soroosh.auth.project.http.controllers;

import com.soroosh.auth.commons.http.HttpStatusResponse;
import com.soroosh.auth.project.context.ApplicationHolderContext;
import com.soroosh.auth.project.http.requests.CreateUserRequest;
import com.soroosh.auth.project.models.ApplicationUser;
import com.soroosh.auth.project.services.ApplicationUserService;
import com.soroosh.auth.user.http.requests.LoginRequest;
import com.soroosh.auth.user.http.requests.SendOtpRequest;
import com.soroosh.auth.user.http.resources.OtpSent;
import com.soroosh.auth.user.services.otp.OTPService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/service/{app}/api/v1")
public class ApplicationUserController {

    private final ApplicationUserService authenticationService;
    private final OTPService otpService;

    @Autowired
    public ApplicationUserController(ApplicationUserService authenticationService, OTPService otpService) {
        this.authenticationService = authenticationService;
        this.otpService = otpService;
    }

    @PostMapping("/otp/request")
    @Operation(security = {@SecurityRequirement(name = "app-id")}, summary = "Send Otp")
    public OtpSent requestOtp(@Validated @RequestBody SendOtpRequest request) {
        UUID verificationID = this.otpService.sendOtp(request.identifier(), ApplicationHolderContext.getApplication().getId());
        return new OtpSent(true, verificationID.toString());
    }


    @PostMapping("/register")
    @Operation(security = {@SecurityRequirement(name = "app-id")}, summary = "Register")
    public HttpStatusResponse<ApplicationUser> register(@Validated @RequestBody CreateUserRequest request) {
        return new HttpStatusResponse<>(true, this.authenticationService.createUser(request.code(), request.identifier(), ApplicationHolderContext.getApplication(), request.user()));
    }

    @PostMapping("/login")
    @Operation(security = {@SecurityRequirement(name = "app-id")}, summary = "Login to Application")
    public HttpStatusResponse<String> login(@RequestParam("app") String app, @RequestBody @Valid LoginRequest request) {
        ApplicationUser user = this.authenticationService.loadByUsernameOrEmail(request.email(), app);
        return new HttpStatusResponse<>(true, null, this.authenticationService.authenticate(user, request.password()));
    }

}

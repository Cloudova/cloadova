package com.cloudova.service.user.http.controllers;

import com.cloudova.service.commons.http.HttpStatusResponse;
import com.cloudova.service.user.http.requests.SendOtpRequest;
import com.cloudova.service.user.http.requests.VerifyOtpRequest;
import com.cloudova.service.user.http.resources.OtpSent;
import com.cloudova.service.user.services.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final OTPService otpService;

    @Autowired
    public AuthenticationController(OTPService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/otp/request")
    public OtpSent requestOtp(@Validated @RequestBody SendOtpRequest request) {
        UUID verificationID = this.otpService.SendOtp(request.identifier());
        return new OtpSent(true, verificationID.toString());
    }

    @PostMapping("/otp/verify")
    public HttpStatusResponse<Void> verifyOtp(@Validated @RequestBody VerifyOtpRequest request) {
        this.otpService.verifyOTP(request.identifier(), request.code());
        return new HttpStatusResponse<>(true);
    }

}

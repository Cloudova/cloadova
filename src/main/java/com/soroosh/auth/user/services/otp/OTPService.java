package com.soroosh.auth.user.services.otp;

import com.soroosh.auth.notification.TransactionalMessagingService;
import com.soroosh.auth.user.exceptions.InvalidOtpException;
import com.soroosh.auth.user.models.OTP;
import com.soroosh.auth.user.repositories.OTPRepository;
import com.soroosh.auth.utils.SecureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class OTPService {

    private final TransactionalMessagingService messagingService;
    private final OTPRepository repository;
    private final Environment environment;

    @Autowired
    public OTPService(TransactionalMessagingService emailService, OTPRepository repository, Environment environment) {
        this.messagingService = emailService;
        this.repository = repository;
        this.environment = environment;
    }

    public UUID sendOtp(String to) {
        String code = SecureUtils.generateRandomNumber(6, (str) -> !this.repository.existsByToken(str));
        OTP otp = this.repository.saveAndFlush(OTP.builder().identifier(to).token(code).build());
        this.messagingService.sendVerification(to, code);
        return otp.getVerificationToken();
    }

    public UUID sendOtp(String to, String appId) {
        String code = SecureUtils.generateRandomNumber(6, (str) -> !this.repository.existsByToken(str));
        OTP otp = this.repository.saveAndFlush(OTP.builder().identifier(to).appId(appId).token(code).build());
        this.messagingService.sendVerification(to, code);
        return otp.getVerificationToken();
    }

    public void verifyOTP(String identifier, String code) {
        if(Arrays.asList(this.environment.getActiveProfiles()).contains("testing")){return;}
        OTP otp = this.repository.findByTokenAndIdentifier(code, identifier).orElseThrow(() -> new InvalidOtpException("Otp Code is not valid: " + code));
        otp.verify();
        this.repository.save(otp);
    }

    public void verifyOTP(String identifier, String code, String appId) {
        if(Arrays.asList(this.environment.getActiveProfiles()).contains("testing")){return;}
        OTP otp = this.repository.findByTokenAndIdentifierForApplication(code, identifier, appId).orElseThrow(() -> new InvalidOtpException("Otp Code is not valid: " + code));
        otp.verify();
        this.repository.save(otp);
    }
}

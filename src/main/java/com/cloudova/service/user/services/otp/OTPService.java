package com.cloudova.service.user.services.otp;

import com.cloudova.service.notification.TransactionalMessagingService;
import com.cloudova.service.user.exceptions.InvalidOtpException;
import com.cloudova.service.user.models.OTP;
import com.cloudova.service.user.repositories.OTPRepository;
import com.cloudova.service.utils.SecureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OTPService {

    private final TransactionalMessagingService messagingService;
    private final OTPRepository repository;

    @Autowired
    public OTPService(TransactionalMessagingService emailService, OTPRepository repository) {
        this.messagingService = emailService;
        this.repository = repository;
    }

    public UUID SendOtp(String to) {
        String code = SecureUtils.generateRandomString(8, (str) -> !this.repository.existsByToken(str));
        OTP otp = this.repository.save(OTP.builder().identifier(to).token(code).build());
        this.messagingService.sendTransactionalMessage(to, "Verification Code", code);
        return otp.getId();
    }

    public void verifyOTP(String identifier, String code) {
        OTP otp = this.repository.findByTokenAndIdentifier(code, identifier).orElseThrow(() -> new InvalidOtpException("Otp Code is not valid"));
        otp.verify();
        this.repository.save(otp);
    }
}

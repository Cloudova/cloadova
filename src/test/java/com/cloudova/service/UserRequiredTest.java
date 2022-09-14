package com.cloudova.service;

import com.cloudova.service.user.models.User;
import com.cloudova.service.user.models.UserDto;
import com.cloudova.service.user.services.UserService;
import com.cloudova.service.user.services.otp.OTPService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.MimeMessage;

import static java.util.concurrent.TimeUnit.SECONDS;

public class UserRequiredTest extends BaseTest{

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
            .withPerMethodLifecycle(false);
    @Autowired
    private OTPService otpService;

    @Autowired
    private UserService userService;

    protected User user;

    @BeforeEach
    void setUp() {
        String email = this.faker.internet().emailAddress();
        this.otpService.SendOtp(email);
        Awaitility.await().atMost(1, SECONDS).until(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            MimeMessage receivedMessage = receivedMessages[receivedMessages.length - 1];
            String code = GreenMailUtil.getBody(receivedMessage).replace("Your Verification Code is: ", "");

            this.user = this.userService.createUser(code, email, new UserDto(
                    this.faker.name().firstName(),
                    this.faker.name().lastName(),
                    email,
                    null,
                    this.faker.internet().password()
            ));
            return true;
        });
    }


}

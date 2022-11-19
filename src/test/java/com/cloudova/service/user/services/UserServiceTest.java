package com.cloudova.service.user.services;


import com.cloudova.service.BaseTest;
import com.cloudova.service.user.models.User;
import com.cloudova.service.user.models.UserDto;
import com.cloudova.service.user.services.otp.OTPService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.internet.MimeMessage;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
class UserServiceTest extends BaseTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
            .withPerMethodLifecycle(false);
    @Autowired
    private OTPService otpService;

    @Autowired
    private UserService userService;

    @Test
    void test_create_user() {
        String email = this.faker.internet().emailAddress();
        this.otpService.sendOtp(email);
        Awaitility.await().atMost(1, SECONDS).until(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            MimeMessage receivedMessage = receivedMessages[receivedMessages.length - 1];
            String code = GreenMailUtil.getBody(receivedMessage).replace("Your Verification Code is: ", "");

            User user = this.userService.createUser(code, email, new UserDto(
                    this.faker.name().firstName(),
                    this.faker.name().lastName(),
                    email,
                    null,
                    this.faker.internet().password()
            ));
            UserDetails userDetails = this.userService.loadUserByUsername(email);
            Assertions.assertEquals(user.getUsername(), userDetails.getUsername());
            return true;
        });
    }

}
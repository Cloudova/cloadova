package com.soroosh.auth;

import com.soroosh.auth.jwt.services.JWTService;
import com.soroosh.auth.user.models.User;
import com.soroosh.auth.user.models.UserDto;
import com.soroosh.auth.user.services.UserService;
import com.soroosh.auth.user.services.otp.OTPService;
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

    @Autowired
    private JWTService jWTService;

    protected String token;
    protected User user;

    @BeforeEach
    void setUp() {
        String email = this.faker.internet().emailAddress();
        this.otpService.sendOtp(email);
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
            this.token = this.jWTService.generateToken(this.user);
            return true;
        });
    }


}

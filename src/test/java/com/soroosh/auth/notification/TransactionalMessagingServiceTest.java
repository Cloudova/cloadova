package com.soroosh.auth.notification;

import com.soroosh.auth.BaseTest;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.internet.MimeMessage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
class TransactionalMessagingServiceTest extends BaseTest {
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
            .withPerMethodLifecycle(false);

    @Autowired
    private TransactionalMessagingService service;

    @Test
    void test_send_transactional_email() {
        String emailBody = "Your Verification Code is: " + this.faker.numerify("#####");
        String emailAddress = this.faker.internet().emailAddress();
        this.service.sendTransactionalMessage(
                emailAddress,
                "Verification",
                emailBody);
        Awaitility.await().atMost(10, SECONDS).untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            MimeMessage receivedMessage = receivedMessages[receivedMessages.length - 1];
            assertEquals(emailBody, GreenMailUtil.getBody(receivedMessage));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals(emailAddress, receivedMessage.getAllRecipients()[0].toString());
        });
    }

    @Test
    void test_send_verification_email() {
        String emailBody = this.faker.numerify("#####");
        String emailAddress = this.faker.internet().emailAddress();
        this.service.sendVerification(emailAddress, emailBody);
        Awaitility.await().atMost(10, SECONDS).untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            MimeMessage receivedMessage = receivedMessages[receivedMessages.length - 1];
            assertEquals("Your Verification Code is: " + emailBody, GreenMailUtil.getBody(receivedMessage));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals(emailAddress, receivedMessage.getAllRecipients()[0].toString());
        });
    }
}
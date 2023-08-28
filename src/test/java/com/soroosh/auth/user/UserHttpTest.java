package com.soroosh.auth.user;

import com.soroosh.auth.BaseTest;
import com.soroosh.auth.user.http.requests.CreateUserRequest;
import com.soroosh.auth.user.models.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.mail.internet.MimeMessage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class UserHttpTest extends BaseTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
            .withPerMethodLifecycle(false);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testCanSignup() throws Exception {
        String email = this.faker.internet().emailAddress();
        this.mockMvc.perform(post("/api/v1/auth/otp/request")
                        .contentType(APPLICATION_JSON)
                        .content("{\"identifier\":\"%s\"}".formatted(email)))
                .andExpect(status().isOk())
                .andExpectAll(result -> Awaitility.await().atMost(1, SECONDS).until(() -> {
                    MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
                    MimeMessage receivedMessage = receivedMessages[receivedMessages.length - 1];
                    String code = GreenMailUtil.getBody(receivedMessage).replace("Your Verification Code is: ", "");
                    String password = this.faker.internet().password();
                    UserDto userDto = new UserDto(
                            this.faker.name().firstName(),
                            this.faker.name().lastName(),
                            email,
                            this.faker.phoneNumber().phoneNumber(),
                            password
                    );
                    this.mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(APPLICATION_JSON)
                            .content(this.objectMapper.writeValueAsString(new CreateUserRequest(code, email, userDto)))
                    ).andExpect(status().isOk());

                    this.mockMvc.perform(post("/api/v1/auth/login")
                                    .contentType(APPLICATION_JSON)
                                    .content("{\"email\":\"%s\", \"password\":\"%s\"}".formatted(email, password))
                            ).andDo(print())
                            .andExpect(status().isOk());

                    return true;
                }));
    }

}
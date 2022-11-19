package com.cloudova.service.project.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cloudova.service.BaseTest;
import com.cloudova.service.jwt.services.JWTService;
import com.cloudova.service.jwt.services.JWTTokenTypes;
import com.cloudova.service.project.dto.ApplicationUserDto;
import com.cloudova.service.project.models.Application;
import com.cloudova.service.project.models.ApplicationUser;
import com.cloudova.service.user.models.UserDto;
import com.cloudova.service.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("testing")
class AuthenticationServiceTest extends BaseTest {

    private Application application;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        String email = this.faker.internet().emailAddress();
        this.application = applicationService.createApplication(this.userService.createUser("code", email, new UserDto(
                this.faker.name().firstName(),
                this.faker.name().lastName(),
                email,
                null,
                this.faker.internet().password()
        )), this.faker.space().company(), this.faker.internet().domainWord());
    }

    @Test
    void loadByUsernameOrEmail() {
        String email = this.faker.internet().emailAddress();
        String username = this.faker.name().username();
        ApplicationUser user = this.applicationUserService.createUser("", email, this.application, ApplicationUserDto.builder().
                username(username)
                .password(this.faker.internet().password())
                .name(this.faker.name().fullName())
                .build());

        ApplicationUser loadedUser = this.applicationUserService.loadByUsernameOrEmail(email, this.application.getSubdomain());
        assertEquals(user.getId(), loadedUser.getId());
        assertEquals(user.getId(), this.applicationUserService.loadByUsernameOrEmail(username, this.application.getSubdomain()).getId());
    }

    @Test
    void authenticate() {
        String email = this.faker.internet().emailAddress();
        String username = this.faker.name().username();
        String password = this.faker.internet().password();
        ApplicationUser user = this.applicationUserService.createUser("", email, this.application, ApplicationUserDto.builder().
                username(username)
                .password(password)
                .name(this.faker.name().fullName())
                .build());
        String jwtToken = this.applicationUserService.authenticate(user, password);
        DecodedJWT decodedJWT = this.jwtService.validateJWT(jwtToken);
        assertEquals(JWTTokenTypes.APPLICATION_TOKEN.name(), decodedJWT.getClaim("type").asString());
        assertEquals(user.getEmail(), decodedJWT.getClaim("preferred_username").asString());
        assertEquals(user.toHashMap().get("email"), decodedJWT.getClaim("user").asMap().get("email"));
        assertEquals(user.toHashMap().get("id").toString(), decodedJWT.getClaim("user").asMap().get("id").toString());
        assertEquals(user.toHashMap().get("name"), decodedJWT.getClaim("user").asMap().get("name"));
    }
}
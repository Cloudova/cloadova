package com.soroosh.auth;

import com.github.javafaker.Faker;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ActiveProfiles;

@EnableJdbcRepositories
@ActiveProfiles({"testing"})
public class BaseTest {

    protected Faker faker;

    public BaseTest() {
        this.faker = new Faker();
    }

}

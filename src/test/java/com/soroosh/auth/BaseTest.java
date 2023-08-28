package com.soroosh.auth;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    protected Faker faker;

    public BaseTest() {
        this.faker = new Faker();
    }
}

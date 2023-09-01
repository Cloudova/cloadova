package com.soroosh.auth;

import com.github.javafaker.Faker;

public class BaseTest {

    protected Faker faker;

    public BaseTest() {
        this.faker = new Faker();
    }

}

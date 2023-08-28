package com.soroosh.auth.jwt.models;

public record JwtKey(String kty, String use, String kid, String alg, String n) {
}

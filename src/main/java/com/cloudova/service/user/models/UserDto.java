package com.cloudova.service.user.models;

import lombok.Data;

import java.io.Serializable;

public record UserDto(String firstName, String lastName, String email,
                      String mobile, String password) implements Serializable {
}

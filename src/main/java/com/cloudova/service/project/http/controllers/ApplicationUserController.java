package com.cloudova.service.project.http.controllers;

import com.cloudova.service.commons.http.HttpStatusResponse;
import com.cloudova.service.user.http.requests.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/service/{app}/api/v1")
public class ApplicationUserController {

    @PostMapping("/login")
    @Operation(security = {@SecurityRequirement(name = "app-id")}, summary = "Login to Application")
    public HttpStatusResponse<String> login(@RequestBody @Valid LoginRequest request) {
        System.out.println("HI");
        return null;
    }

}

package com.cloudova.service.project.http.controllers;

import com.cloudova.service.commons.http.HttpStatusResponse;
import com.cloudova.service.project.http.requests.CreateApplicationRequest;
import com.cloudova.service.project.models.Application;
import com.cloudova.service.project.services.ApplicationService;
import com.cloudova.service.user.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public HttpStatusResponse<Application> createNewApplication(@Validated @RequestBody CreateApplicationRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Application application = this.applicationService.createApplication(user, request.name(), request.subdomain(), request.description());
        return new HttpStatusResponse<>(true, application);
    }

}

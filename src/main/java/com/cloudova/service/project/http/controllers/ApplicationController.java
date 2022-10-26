package com.cloudova.service.project.http.controllers;

import com.cloudova.service.commons.http.HttpStatusResponse;
import com.cloudova.service.project.http.requests.CreateApplicationRequest;
import com.cloudova.service.project.models.Application;
import com.cloudova.service.project.services.ApplicationService;
import com.cloudova.service.user.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static com.cloudova.service.utils.AuthenticationUtils.getCurrentUser;

@RestController
@Tag(name = "Application", description = "create, delete and update applications")
@RequestMapping("/api/v1/applications")
@Validated
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Create new application")
    public HttpStatusResponse<Application> createNewApplication(@Validated @RequestBody CreateApplicationRequest request) {
        User user = getCurrentUser();
        Application application = this.applicationService.createApplication(user, request.name(), request.subdomain(), request.description());
        return new HttpStatusResponse<>(true, application);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Applications List")
    public HttpStatusResponse<Page<Application>> list(@RequestParam(name = "page", defaultValue = "1") @Min(1) int page,
                                                      @RequestParam(name = "perPage", defaultValue = "10") @Min(5) int perPage,
                                                      @RequestParam(name = "direction", defaultValue = "DESC") Sort.Direction direction,
                                                      @RequestParam(name = "sortBy",required = false) String sortBy) {
        User user = getCurrentUser();
        PageRequest pagination = PageRequest.of(page-1, perPage,
                sortBy != null ? Sort.by(direction, sortBy) : Sort.unsorted());
        return new HttpStatusResponse<>(true, this.applicationService.list(
                user.getId(),
                pagination
        ));
    }

}

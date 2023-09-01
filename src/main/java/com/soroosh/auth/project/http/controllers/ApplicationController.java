package com.soroosh.auth.project.http.controllers;

import com.soroosh.auth.commons.http.HttpStatusResponse;
import com.soroosh.auth.project.dto.ApplicationDto;
import com.soroosh.auth.project.models.Application;
import com.soroosh.auth.project.services.ApplicationService;
import com.soroosh.auth.user.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.soroosh.auth.utils.AuthenticationUtils.getCurrentUser;

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
    @PreAuthorize("hasAuthority('create:application')")
    public HttpStatusResponse<Application> createNewApplication(@Validated @RequestBody ApplicationDto request) {
        User user = getCurrentUser();
        Application application = this.applicationService.createApplication(user, request.name(), request.subdomain(), request.description());
        return new HttpStatusResponse<>(true, application);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Applications List")
    @PreAuthorize("hasAuthority('list:application')")
    public HttpStatusResponse<Page<Application>> list(@RequestParam(name = "page", defaultValue = "1") @Min(1) int page,
                                                      @RequestParam(name = "perPage", defaultValue = "10") @Min(5) int perPage,
                                                      @RequestParam(name = "direction", defaultValue = "DESC") Sort.Direction direction,
                                                      @RequestParam(name = "sortBy", required = false) String sortBy) {
        User user = getCurrentUser();
        PageRequest pagination = PageRequest.of(page - 1, perPage,
                sortBy != null ? Sort.by(direction, sortBy) : Sort.unsorted());
        return new HttpStatusResponse<>(true, this.applicationService.list(
                user.getId(),
                pagination
        ));
    }


    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Applications List")
    @PreAuthorize("hasAuthority('delete:application')")
    public HttpStatusResponse<String> delete(@PathVariable String id) {
        Application application = this.applicationService.getById(id).orElseThrow();
        this.applicationService.deleteApplication(application);
        return new HttpStatusResponse<>(true);
    }

    @PutMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Update Application")
    @PreAuthorize("hasAuthority('update:application')")
    public HttpStatusResponse<Application> update(@PathVariable String id, @Validated @RequestBody ApplicationDto applicationDto) {
        Application application = this.applicationService.getById(id).orElseThrow();
        return new HttpStatusResponse<>(true, this.applicationService.updateApplication(application, applicationDto));
    }


}

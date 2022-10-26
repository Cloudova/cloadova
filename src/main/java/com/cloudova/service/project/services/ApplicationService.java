package com.cloudova.service.project.services;

import com.cloudova.service.commons.exceptions.InvalidArgumentException;
import com.cloudova.service.commons.exceptions.LimitExceedException;
import com.cloudova.service.project.models.Application;
import com.cloudova.service.project.repositories.ApplicationRepository;
import com.cloudova.service.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
/*
 *  - Create New Application
 *
 */
public class ApplicationService {

    private final ApplicationRepository repository;

    @Value("${restrictions.max_app_for_user}")
    private int maxAppForUser;

    @Autowired
    public ApplicationService(ApplicationRepository repository) {
        this.repository = repository;
    }

    public Application createApplication(User user, String name, String subdomain) {
        return this.createApplication(user, name, subdomain, null);
    }

    public Page<Application> list(Long id, Pageable pageable) {
        return this.repository.findByUserId(id, pageable);
    }

    public Application createApplication(User user, String name, String subdomain, String description) {
        if (this.repository.countByUser_Id(user.getId()) >= this.maxAppForUser) {
            throw new LimitExceedException("You can't create more than %d applications".formatted(this.maxAppForUser));
        }
        if (this.repository.existsBySubdomain(subdomain)) {
            throw new InvalidArgumentException("Entered subdomain is taken");
        }

        Application application = Application.builder()
                .user(user)
                .name(name)
                .subdomain(subdomain)
                .description(description)
                .build();

        return this.repository.save(application);
    }
}

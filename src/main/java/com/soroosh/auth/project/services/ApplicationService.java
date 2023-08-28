package com.soroosh.auth.project.services;

import com.soroosh.auth.commons.exceptions.InvalidArgumentException;
import com.soroosh.auth.commons.exceptions.LimitExceedException;
import com.soroosh.auth.project.dto.ApplicationDto;
import com.soroosh.auth.project.models.Application;
import com.soroosh.auth.project.repositories.ApplicationRepository;
import com.soroosh.auth.user.models.User;
import com.soroosh.auth.utils.SecureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
/*
 *  - Create New Application
 *
 */
public class ApplicationService {

    private final ApplicationRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Value("${restrictions.max_app_for_user}")
    private int maxAppForUser;

    @Autowired
    public ApplicationService(ApplicationRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Application createApplication(User user, String name, String subdomain) {
        return this.createApplication(user, name, subdomain, null);
    }

    public Page<Application> list(Long id, Pageable pageable) {
        return this.repository.findByUserId(id, pageable);
    }

    @PreAuthorize("#application.user.id == authentication.principal.id")
    public void deleteApplication(Application application) {
        this.repository.delete(application);
    }

    @PreAuthorize("#application.user.id == authentication.principal.id")
    public Application updateApplication(Application application, ApplicationDto dto) {
        if (!dto.subdomain().equals(application.getSubdomain()) && this.repository.existsBySubdomain(dto.subdomain())) {
            throw new InvalidArgumentException("Entered subdomain is taken");
        }

        application.setName(dto.name());
        application.setDescription(dto.description());
        application.setSubdomain(dto.subdomain());
        return this.repository.save(application);
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
                .secret(this.passwordEncoder.encode(SecureUtils.generateRandomString(64, (token) -> true)))
                .build();

        return this.repository.save(application);
    }

    public Optional<Application> getById(String id) {
        return this.repository.findById(id);
    }
}

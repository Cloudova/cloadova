package com.cloudova.service.config.routing;

import com.cloudova.service.project.filters.ApplicationSecretFilter;
import com.cloudova.service.project.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class FilterConfig {

    private final ApplicationService applicationService;

    @Autowired
    public FilterConfig(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Bean
    public FilterRegistrationBean<ApplicationSecretFilter> filterRegistrationBean() {
        FilterRegistrationBean<ApplicationSecretFilter> registrationBean = new FilterRegistrationBean<>();
        ApplicationSecretFilter applicationSecretFilter = new ApplicationSecretFilter(this.applicationService);

        registrationBean.setFilter(applicationSecretFilter);
        registrationBean.addUrlPatterns("/service/$1/api/v1");
        return registrationBean;
    }
}

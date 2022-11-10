package com.cloudova.service.project.filters;


import com.cloudova.service.project.context.ApplicationHolderContext;
import com.cloudova.service.project.models.Application;
import com.cloudova.service.project.services.ApplicationService;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class ApplicationSecretFilter implements Filter {

    public static final String applicationRequestsFormat = "/service/{app}/api/v1/**";

    private final ApplicationService applicationService;

    public ApplicationSecretFilter(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        var parser = new PathPatternParser();
        var pp = parser.parse(ApplicationSecretFilter.applicationRequestsFormat);

        if(!pp.matches(PathContainer.parsePath(request.getServletPath()))){
            filterChain.doFilter(request, response);
            return;
        }

        String application = Optional.ofNullable(request.getHeader("app-id")).orElse("");
        Optional<Application> app = this.applicationService.getById(application);
        if (app.isPresent()) {
            var info = pp.matchAndExtract(PathContainer.parsePath(request.getServletPath()));
            assert info != null;
            String appName = info.getUriVariables().getOrDefault("app", null);

            if(!app.get().getSubdomain().equals(appName)){
                response.setStatus(404);
                return;
            }
            ApplicationHolderContext.setCurrentApplication(app.get());
            response.setHeader("app-name", app.get().getSubdomain());
            //call next filter in the filter chain
            filterChain.doFilter(request, response);
        }
        response.setStatus(404);
    }
}

package com.cloudova.service.project.context;

import com.cloudova.service.project.models.Application;
import org.springframework.util.Assert;

public final class ApplicationHolderContext {
    private static final ThreadLocal<Application> application = new ThreadLocal<>();

    public static void setCurrentApplication(Application application) {
        Assert.notNull(application, "Application can not be null");
        ApplicationHolderContext.application.set(application);
    }

    public static Application getApplication() {
        return ApplicationHolderContext.application.get();
    }

    public static void clear(){
        ApplicationHolderContext.application.remove();
    }

}

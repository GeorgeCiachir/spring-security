package com.georgeciachir.ch5_implementing_authentication.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableAsync
@Configuration
public class AsyncSecurityConfig {

    // this can also be done in the application.properties file, by setting the property
    // spring.security.strategy=MODE_GLOBAL
    @Profile("ansmts")
    @Bean
    public InitializingBean initializingBeanAsyncNotSpringManaged() {
        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
    }

    // this can also be done in the application.properties file, by setting the property
    // spring.security.strategy=MODE_INHERITABLETHREADLOCAL
    @Profile("asmts")
    @Bean
    public InitializingBean initializingBeanAsyncSpringManaged() {
        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}

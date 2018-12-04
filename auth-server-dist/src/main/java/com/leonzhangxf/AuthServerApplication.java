package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
@Import({
        SwaggerConfiguration.class,
        AuthorizationServerConfiguration.class,
        WebSecurityConfiguration.class,
})
@SessionAttributes("authorizationRequest")
public class AuthServerApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthServerApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }
}

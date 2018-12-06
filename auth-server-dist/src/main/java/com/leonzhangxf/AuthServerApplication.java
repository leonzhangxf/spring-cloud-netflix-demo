package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * authserver参考项目
 * https://github.com/spring-cloud-samples/authserver.git
 * <p>
 * sso参考项目
 * https://github.com/spring-cloud-samples/sso.git
 */
@SpringBootApplication
@EnableDiscoveryClient
@Import({
        SwaggerConfiguration.class,
        AuthorizationServerConfiguration.class,
        WebSecurityConfiguration.class,
})
@SessionAttributes("authorizationRequest")
@Controller
public class AuthServerApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthServerApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/auth/login").setViewName("login");
        registry.addViewController("/auth/oauth/confirm_access").setViewName("authorize");
        registry.addViewController("/auth/oauth/error").setViewName("error");
    }
}

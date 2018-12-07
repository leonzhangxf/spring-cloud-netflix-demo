package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
@Import({
        SwaggerConfiguration.class,
})
public class ClientDemoAApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientDemoAApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}

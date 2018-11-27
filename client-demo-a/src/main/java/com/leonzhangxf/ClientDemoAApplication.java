package com.leonzhangxf;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ClientDemoAApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientDemoAApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}

package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Import({
        SwaggerConfiguration.class
})
public class ClientDemoBApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientDemoBApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}

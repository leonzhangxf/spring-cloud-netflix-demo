package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@Import({
        SwaggerConfiguration.class
})
@RestController
@RefreshScope
public class ClientDemoAApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientDemoAApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

    private String value;

    @GetMapping("value")
    public ResponseEntity<String> value() {
        System.out.println(value);
        return ResponseEntity.ok(value);
    }

    @Value("${swagger.title:aaaaa}")
    public void setValue(String value) {
        this.value = value;
    }
}

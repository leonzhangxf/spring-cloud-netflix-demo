package com.leonzhangxf;

import com.leonzhangxf.configuration.SwaggerConfiguration;
import com.leonzhangxf.manager.client.ClientDemoAClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Import({
        SwaggerConfiguration.class
})
@RestController
public class ClientDemoBApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientDemoBApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

    private static Logger logger = LoggerFactory.getLogger(ClientDemoBApplication.class);

    private ClientDemoAClient clientDemoAClient;

    @GetMapping("value")
    public String clientDemoAValue() {
        String value = clientDemoAClient.value();
        logger.debug("The value of client demo a is {}.", value);
        return value;
    }

    @Autowired
    public void setClientDemoAClient(ClientDemoAClient clientDemoAClient) {
        this.clientDemoAClient = clientDemoAClient;
    }
}

package com.leonzhangxf.manager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "client-demo-a")
@Component
public interface ClientDemoAClient {

    @GetMapping("/value")
    String value();
}

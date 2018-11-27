package com.leonzhangxf.manager.client;

import com.leonzhangxf.domain.dto.ValueBeanDTO;
import com.leonzhangxf.util.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("client-demo-a")
@Component
public interface ClientDemoAClient {

    @GetMapping("/value")
    ApiResponse<String> value();

    @GetMapping("bean/value")
    ApiResponse<ValueBeanDTO> valueBean();
}

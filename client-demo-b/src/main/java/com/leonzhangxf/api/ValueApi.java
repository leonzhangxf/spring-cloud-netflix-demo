package com.leonzhangxf.api;

import com.leonzhangxf.domain.dto.ValueBeanDTO;
import com.leonzhangxf.manager.client.ClientDemoAClient;
import com.leonzhangxf.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "从B获取A的value测试API")
@RestController
public class ValueApi {

    private Logger logger = LoggerFactory.getLogger(ValueApi.class);

    private ClientDemoAClient clientDemoAClient;

    @ApiOperation("获取value")
    @GetMapping("value")
    public ApiResponse<String> value() {
        ApiResponse<String> response = clientDemoAClient.value();
        logger.debug("The value is {}.", response);
        return response;
    }

    @ApiOperation("获取value实体")
    @GetMapping("bean/value")
    public ApiResponse<ValueBeanDTO> valueBean() {
        ApiResponse<ValueBeanDTO> response = clientDemoAClient.valueBean();
        logger.debug("The value bean is {}.", response);
        return response;
    }

    @Autowired
    public void setClientDemoAClient(ClientDemoAClient clientDemoAClient) {
        this.clientDemoAClient = clientDemoAClient;
    }
}

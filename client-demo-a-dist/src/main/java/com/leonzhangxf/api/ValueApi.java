package com.leonzhangxf.api;

import com.leonzhangxf.domain.dto.ValueBeanDTO;
import com.leonzhangxf.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "value获取测试API")
@RestController
@RefreshScope
public class ValueApi {

    private Logger logger = LoggerFactory.getLogger(ValueApi.class);

    private String value;

    @ApiOperation("获取value")
    @GetMapping("value")
    public ResponseEntity<ApiResponse<String>> value() {
        logger.debug("The inject value is {}.", value);
        return ResponseEntity.badRequest().body(ApiResponse.ok(value));
    }

    @ApiOperation("获取value实体")
    @GetMapping("bean/value")
    public ApiResponse<ValueBeanDTO> valueBean() {
        ValueBeanDTO bean = new ValueBeanDTO();
        bean.setStr("Leon");
        bean.setInte(666);
        return ApiResponse.ok(bean);
    }

    @Value("${swagger.title:DemoA文档}")
    public void setValue(String value) {
        this.value = value;
    }
}

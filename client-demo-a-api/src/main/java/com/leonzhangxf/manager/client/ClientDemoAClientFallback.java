package com.leonzhangxf.manager.client;

import com.leonzhangxf.domain.dto.ValueBeanDTO;
import com.leonzhangxf.util.ApiResponse;
import org.springframework.stereotype.Component;

/**
 * Fallback 实现，在正常服务调用失败时调用返回。
 * 可以返回异常标识数据，静态数据，或者进行补偿处理或调用。
 */
@Component
public class ClientDemoAClientFallback implements ClientDemoAClient {

    @Override
    public ApiResponse<String> value() {
        return ApiResponse.internalServerError("", null);
    }

    @Override
    public ApiResponse<ValueBeanDTO> valueBean() {
        return ApiResponse.internalServerError("", null);
    }
}

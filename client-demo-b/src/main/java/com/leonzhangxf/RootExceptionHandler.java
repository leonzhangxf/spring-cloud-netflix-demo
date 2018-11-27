package com.leonzhangxf;

import com.leonzhangxf.util.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RootExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(RootExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ApiResponse<String> rootExceptionHandler(RuntimeException ex) {
        logger.error("RuntimeException: ", ex);
        return ApiResponse.internalServerError("服务器大爷跑了~~", null);
    }
}

package com.leonzhangxf.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@ApiModel("API响应封装")
public final class ApiResponse<T extends Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("响应状态码，成功返回200")
    private int status;

    @ApiModelProperty("响应信息")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;

    @ApiModelProperty("时间戳")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp;

    private ApiResponse() {
    }

    public static <T> ApiResponse<T> ok(T data) {
        return response(HttpStatus.OK, null, data);
    }

    public static <T> ApiResponse<T> badReqeust(String message, T data) {
        return response(HttpStatus.BAD_REQUEST, message, data);
    }

    public static <T> ApiResponse<T> unauthorized(String message, T data) {
        return response(HttpStatus.UNAUTHORIZED, message, data);
    }

    public static <T> ApiResponse<T> forbidden(String message, T data) {
        return response(HttpStatus.FORBIDDEN, message, data);
    }

    public static <T> ApiResponse<T> internalServerError(String message, T data) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, message, data);
    }

    public static <T> ApiResponse<T> response(HttpStatus status, String message, T data) {
        message = StringUtils.hasText(message) ? message : status.getReasonPhrase();

        ApiResponse<T> res = new ApiResponse<T>();
        res.setStatus(status.value());
        res.setMessage(message);
        res.setData(data);
        res.setTimestamp(new Date());
        return res;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse<?> that = (ApiResponse<?>) o;
        return status == that.status &&
                Objects.equals(message, that.message) &&
                Objects.equals(data, that.data) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, data, timestamp);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

}

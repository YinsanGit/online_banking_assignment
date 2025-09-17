package com.finalBanking.demo.Exception;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

public class ApiResponseUtil {
    public static ApiResponseEntityDto createApiResponseEntityDto(
            String errorStatus,int statusCode, String message, String errorDescription,
            Object responseData) {
        return ApiResponseEntityDto.builder()
                .errorCode(errorStatus)
                .statusCode(statusCode)
                .message(message)
                .messageDescription(errorDescription)
                .timestamp(LocalDateTime.now())
                .responseData(responseData)
                .build();
    }

    public static ApiResponseEntityDto successResponse(Object object) {
        return ApiResponseUtil
                .createApiResponseEntityDto(
                        "200",
                        200,
                        "Success",
                        "Success",
                        object);
    }


}

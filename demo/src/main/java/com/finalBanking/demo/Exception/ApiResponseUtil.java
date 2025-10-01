package com.finalBanking.demo.Exception;

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

    public static Map<String, Object> errorResponse(String message, Object details) {
        return Map.of(
                "success", false,
                "message", message,
                "details", details
        );
    }

    public static ApiResponseEntityDto deleteSuccessResponse(String message) {
        return ApiResponseUtil
                .createApiResponseEntityDto(
                        "200",
                        200,
                        "Deleted Successfully",
                        message,
                        null);
    }


}

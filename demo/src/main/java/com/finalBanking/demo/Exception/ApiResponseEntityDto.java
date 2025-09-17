package com.finalBanking.demo.Exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponseEntityDto {


    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("status_code")
    private int statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("message_description")
    private String messageDescription;
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("response_data")
    private Object responseData;
}



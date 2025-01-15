package com.seq.deleteAccount.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseData<T> {

    private final Integer code = Code.SUCCESS.getCode();
    private String message = Code.SUCCESS.getMessage(); // default message
    private T data;

    public static <T> ApiResponseData<T> of(T data, String message) {
        return ApiResponseData.<T>builder()
                .message(message)
                .data(data)
                .build();
    }
}
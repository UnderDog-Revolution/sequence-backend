package site.sequence.projectservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ApiResponseData<T> extends ApiResponse{

    private T data;

    public static <T> ApiResponseData<T> of(T data) {
        return ApiResponseData.<T>builder()
                .code(Code.SUCCESS.getCode())
                .message(Code.SUCCESS.getMessage())
                .data(data)
                .build();
    }
    public static <T> ApiResponseData<T> of(T data, String message) {
        return ApiResponseData.<T>builder()
                .code(Code.SUCCESS.getCode())
                .message(message)
                .data(data)
                .build();
    }


}
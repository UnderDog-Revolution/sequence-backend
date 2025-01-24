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
public class ApiResponseError extends ApiResponse{

    public static ApiResponseError of(Code code) {
        return ApiResponseError.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();
    }

    public static ApiResponseError of(Code code, String detailMessage) {
        return ApiResponseError.builder()
                .code(code.getCode())
                .message(detailMessage)
                .build();
    }
}

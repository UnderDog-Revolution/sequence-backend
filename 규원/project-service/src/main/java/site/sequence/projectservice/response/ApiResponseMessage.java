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
public class ApiResponseMessage extends ApiResponse {

    public static ApiResponseMessage of(String message) {
        return ApiResponseMessage.builder()
                .code(Code.SUCCESS.getCode())
                .message(message)
                .build();
    }

    public static ApiResponseMessage of() {
        return ApiResponseMessage.builder()
                .code(Code.SUCCESS.getCode())
                .message(Code.SUCCESS.getMessage())
                .build();
    }
}
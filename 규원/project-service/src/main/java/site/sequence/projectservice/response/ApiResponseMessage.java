package site.sequence.projectservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseMessage {

    private final Integer code = Code.SUCCESS.getCode();
    private String message = Code.SUCCESS.getMessage(); // default message

    public static ApiResponseMessage of(String message) {
        return ApiResponseMessage.builder()
                .message(message)
                .build();
    }
}
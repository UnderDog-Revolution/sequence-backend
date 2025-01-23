//package sequence.sequence_member.deleteAccount.dto;
//
//import lombok.*;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ApiResponseError {
//
//    private Integer code;
//    private String message;
//
//    public static ApiResponseError of(Code code) {
//        return ApiResponseError.builder()
//                .code(code.getCode())
//                .message(code.getMessage())
//                .build();
//    }
//
//    public static ApiResponseError of(Code code, String detailMessage) {
//        return ApiResponseError.builder()
//                .code(code.getCode())
//                .message(detailMessage)
//                .build();
//    }
//}

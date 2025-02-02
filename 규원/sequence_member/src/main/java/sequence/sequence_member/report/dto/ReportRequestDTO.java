//package sequence.sequence_member.report.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ReportRequestDTO {
//    @NotBlank(message = "이름을 입력해주세요.")
//    private String name;
//
//    @NotBlank(message = "생년월일을 입력해주세요.")
//    private String birthdate;
//
//    @NotBlank(message = "학력을 입력해주세요.")
//    private String education;
//
//    @NotEmpty(message = "신고 유형을 선택해주세요.")
//    private List<String> reportTypes;
//
//    @NotBlank(message = "신고 내용을 작성해주세요.")
//    @Size(max = 500, message = "신고 내용은 500자 이하로 작성해야 합니다.")
//    private String reportDetail;
//}

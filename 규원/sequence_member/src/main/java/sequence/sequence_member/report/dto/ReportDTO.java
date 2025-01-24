package sequence.sequence_member.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDTO {

    @NotNull(message = "Member ID는 필수 입력값입니다.")
    private Long memberId;

    @NotBlank(message = "신고 유형은 필수 입력값입니다.")
    private String reportType;

    @NotBlank(message = "신고 내용은 필수 입력값입니다.")
    private String description;
}

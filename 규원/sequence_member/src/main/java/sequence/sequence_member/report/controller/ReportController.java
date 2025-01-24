package sequence.sequence_member.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sequence.sequence_member.report.dto.ReportDTO;
import sequence.sequence_member.report.service.ReportService;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<String> createReport(@RequestBody @Valid ReportDTO reportDTO) {
        String responseMessage = reportService.createReport(reportDTO);

        if (responseMessage.equals("신고가 성공적으로 접수되었습니다.")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.badRequest().body(responseMessage);
        }
    }
}

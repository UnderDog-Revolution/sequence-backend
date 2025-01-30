package sequence.sequence_member.report.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sequence.sequence_member.report.dto.ReportDTO;
import sequence.sequence_member.report.entity.ReportEntity;
import sequence.sequence_member.report.repository.ReportRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitReport(@RequestBody ReportDTO reportDTO) {
//        if (reportDTO.getName() == null || reportDTO.getBirthdate() == null ||
//                reportDTO.getEducation() == null || reportDTO.getReportTypes() == null ||
//                reportDTO.getReportDetail() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", "필수 정보를 모두 입력해주세요.", "status", "error"));
//        }

        // 로그 추가
        System.out.println("Received ReportDTO: " + reportDTO);

        ReportEntity reportEntity = new ReportEntity(
                reportDTO.getName(),
                reportDTO.getBirthdate(),
                reportDTO.getEducation(),
                reportDTO.getReportTypes(),
                reportDTO.getReportDetail()
        );

        // 로그 추가
        System.out.println("Converted ReportEntity: " + reportEntity);

        reportRepository.save(reportEntity);

        return ResponseEntity.ok(Map.of("message", "신고가 성공적으로 접수되었습니다.", "status", "success"));
    }
}

//package sequence.sequence_member.report.controller;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//import sequence.sequence_member.report.dto.ReportDTO;
//import sequence.sequence_member.report.service.ReportService;
//
//@RestController
//@RequestMapping("/api/report")
//@RequiredArgsConstructor
//public class ReportController {
//    private final ReportService reportService;
//
//    @PostMapping
//    public ResponseEntity<String> report(@Valid @RequestBody ReportDTO reportDTO,
//                                         @AuthenticationPrincipal UserDetails userDetails) {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
//        }
//
//        String message = reportService.submitReport(reportDTO, userDetails.getUsername());
//        return ResponseEntity.ok(message);
//    }
//}
package sequence.sequence_member.report.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sequence.sequence_member.report.dto.ReportDTO;
import sequence.sequence_member.report.entity.ReportEntity;
import sequence.sequence_member.report.service.ReportService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    //신고 제출 (신고자 ID를 쿠키에서 자동 조회)
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitReport(
            HttpServletRequest request,
            @RequestBody @Valid ReportDTO reportDTO) {

        // 쿠키에서 reporterId 가져오기
        Optional<Long> reporterId = getReporterIdFromCookies(request);

        if (reporterId.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "로그인이 필요합니다.");
            response.put("status", "error");
            return ResponseEntity.status(401).body(response);
        }

        reportDTO.setReporterId(reporterId.get()); // 신고자 ID 자동 설정

        try {
            ReportEntity report = reportService.submitReport(reportDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "신고가 성공적으로 접수되었습니다.");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }


    //특정 신고 내역 조회
    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportById(@PathVariable Long reportId) {
        Optional<ReportEntity> report = reportService.getReportById(reportId);
        return report.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("message", "신고 내역을 찾을 수 없습니다.")));
    }

    //특정 사용자의 신고 내역 조회
    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<?> getReportsByReporter(@PathVariable Long reporterId) {
        List<ReportEntity> reports = reportService.getReportsByReporter(reporterId);
        if (reports.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "해당 사용자가 작성한 신고 내역이 없습니다."));
        }
        return ResponseEntity.ok(reports);
    }

     //쿠키에서 reporterId 가져오는 메서드
    private Optional<Long> getReporterIdFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();

        for (Cookie cookie : cookies) {
            if ("reporterId".equals(cookie.getName())) {
                try {
                    return Optional.of(Long.parseLong(cookie.getValue()));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}

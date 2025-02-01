package sequence.sequence_member.report.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sequence.sequence_member.member.jwt.JWTUtil;
import sequence.sequence_member.member.repository.MemberRepository;
import sequence.sequence_member.report.dto.ReportDTO;
import sequence.sequence_member.report.entity.ReportEntity;
import sequence.sequence_member.report.repository.ReportRepository;
import sequence.sequence_member.member.repository.RefreshRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportRepository reportRepository;
    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public ReportController(ReportRepository reportRepository,
                            RefreshRepository refreshRepository,
                            JWTUtil jwtUtil,
                            MemberRepository memberRepository)
    {
        this.reportRepository = reportRepository;
        this.refreshRepository = refreshRepository;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportEntity> getReport(@PathVariable Long id) {
        return reportRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReportEntity>> getAllReports() {
        return ResponseEntity.ok(reportRepository.findAll());
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitReport(@RequestBody ReportDTO reportDTO, HttpServletRequest request) {
        if (reportDTO.getName() == null || reportDTO.getBirthdate() == null ||
                reportDTO.getEducation() == null || reportDTO.getReportTypes() == null ||
                reportDTO.getReportDetail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "필수 정보를 모두 입력해주세요.", "status", "error"));
        }

        //유효한 토큰인지 확인
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }
        if (refresh == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "토큰을 찾을 수 없습니다.", "status", "error"));
        }
        //DB에 refresh 토큰이 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "토큰이 만료되었습니다.", "status", "error"));
        }

        //신고자의 이름을 쿠키에서 받아옴
        String username = jwtUtil.getUsername(refresh);
        var result = memberRepository.findByUsername(username);
        var externalUser = result.get();
        reportDTO.setReporter(externalUser.getUsername());

//        if( reportRepository.existsByName(externalUser.getUsername()) && reportRepository.existsByReporter(refresh)){};

        //db에 저장
        ReportEntity reportEntity = new ReportEntity(
                reportDTO.getName(),
                reportDTO.getReporter(),
                reportDTO.getBirthdate(),
                reportDTO.getEducation(),
                reportDTO.getReportTypes(),
                reportDTO.getReportDetail()
        );
        reportRepository.save(reportEntity);

        return ResponseEntity.ok(Map.of("message", "신고가 성공적으로 접수되었습니다.", "status", "success"));
    }
}

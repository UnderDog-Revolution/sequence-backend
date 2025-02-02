//package sequence.sequence_member.report.service;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//import sequence.sequence_member.member.entity.MemberEntity;
//import sequence.sequence_member.member.repository.MemberRepository;
//import sequence.sequence_member.report.dto.ReportDTO;
//import sequence.sequence_member.report.entity.ReportEntity;
//import sequence.sequence_member.report.repository.ReportRepository;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ReportService {
//    private final ReportRepository reportRepository;
//    private final MemberRepository memberRepository;
//
//    @Transactional
//    public String submitReport(ReportDTO reportDTO, String username) {
//        Optional<MemberEntity> memberOptional = memberRepository.findByUsername(username);
//        if (memberOptional.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.");
//        }
//
//        ReportEntity report = ReportEntity.builder()
//                .member(memberOptional.get())
//                .reportType(reportDTO.getReportType())
//                .content(reportDTO.getContent())
//                .build();
//
//        reportRepository.save(report);
//        return "신고가 성공적으로 접수되었습니다.";
//    }
//}
package sequence.sequence_member.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sequence.sequence_member.member.entity.MemberEntity;
import sequence.sequence_member.member.repository.MemberRepository;
import sequence.sequence_member.report.dto.ReportDTO;
import sequence.sequence_member.report.entity.ReportEntity;
import sequence.sequence_member.report.repository.ReportRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    public ReportEntity submitReport(ReportDTO reportDTO) {
        if (reportDTO.getReportDetail() == null || reportDTO.getReportDetail().isEmpty()) {
            throw new IllegalArgumentException("신고 내용을 작성해주세요.");
        }

        if (reportDTO.getReportDetail().length() > 500) {
            throw new IllegalArgumentException("신고 내용은 500자 이하로 작성해주세요.");
        }

        // 신고자 조회
        MemberEntity reporter = memberRepository.findById(reportDTO.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("신고자를 찾을 수 없습니다."));

        // 피신고자 조회
        MemberEntity reported = memberRepository.findById(reportDTO.getReportedId())
                .orElseThrow(() -> new IllegalArgumentException("피신고자를 찾을 수 없습니다."));

        // 신고 저장
        ReportEntity reportEntity = ReportEntity.toReportEntity(reporter, reported, reportDTO.getReportTypes(), reportDTO.getReportDetail());
        return reportRepository.save(reportEntity);
    }

    //특정 신고 조회
    public Optional<ReportEntity> getReportById(Long reportId) {
        return reportRepository.findById(reportId);
    }

    //특정 사용자가 작성한 신고 내역 조회
    public List<ReportEntity> getReportsByReporter(Long reporterId) {
        return reportRepository.findByReporterId(reporterId);
    }
}

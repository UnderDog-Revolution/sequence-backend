package sequence.sequence_member.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sequence.sequence_member.member.entity.MemberEntity;
import sequence.sequence_member.member.repository.MemberRepository;
import sequence.sequence_member.report.dto.ReportDTO;
import sequence.sequence_member.report.entity.ReportEntity;
import sequence.sequence_member.report.repository.ReportRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public String createReport(ReportDTO reportDTO) {
        Optional<MemberEntity> optionalMember = memberRepository.findById(reportDTO.getMemberId());

        if (optionalMember.isEmpty()) {
            return "회원 정보를 찾을 수 없습니다.";
        }

        MemberEntity member = optionalMember.get();
        ReportEntity reportEntity = ReportEntity.toEntity(member, reportDTO.getReportType(), reportDTO.getDescription());
        reportRepository.save(reportEntity);

        return "신고가 성공적으로 접수되었습니다.";
    }
}

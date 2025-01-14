package sequence.sequence_member.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import sequence.sequence_member.dto.MemberDTO;
import sequence.sequence_member.repository.MemberRepository;

import java.util.Date;

@Entity
@Data
@Table(name = "award")
public class AwardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long awardId;

    @ManyToOne
    @JoinColumn
    private MemberEntity member;

    @Column
    private String awardName;

    @Column
    @Temporal(TemporalType.DATE)
    private Date awardDuration;

    @Column
    private String awardDescription;

    public static AwardEntity toAwardEntity(MemberDTO memberDTO, MemberEntity memberEntity){
        AwardEntity awardEntity = new AwardEntity();

        awardEntity.setAwardName(memberDTO.getAward_name());
        awardEntity.setAwardDuration(memberDTO.getAward_duration());
        awardEntity.setAwardDescription(memberDTO.getAward_description());
        awardEntity.setMember(memberEntity);

        return awardEntity;
    }

}

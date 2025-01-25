package site.sequence.projectservice.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import site.sequence.projectservice.dto.ProjectDTO;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Period;
import site.sequence.projectservice.enums.Step;

@Entity
@Data
@Table(name = "project")
public class ProjectEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Period period;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Integer personnel;

    @Column(nullable = false)
    private String roles;

    @Column(nullable = false)
    private String skills;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MeetingOption meeting_option;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Step step;

    @Column(nullable = false)
    private String members;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String introduce;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String article;

    private String link;

    @Column(nullable = false)
    private String writer;

    public static ProjectEntity toProjectEntity(ProjectDTO projectDTO){
        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setTitle(projectDTO.getTitle());
        projectEntity.setPeriod(projectDTO.getPeriod());
        projectEntity.setCategory(projectDTO.getCategory());
        projectEntity.setPersonnel(projectDTO.getPersonnel());
        projectEntity.setRoles(projectDTO.getRoles());
        projectEntity.setSkills(projectDTO.getSkills());
        projectEntity.setMeeting_option(projectDTO.getMeeting_option());
        projectEntity.setStep(projectDTO.getStep());
        projectEntity.setMembers(projectDTO.getMembers());
        projectEntity.setIntroduce(projectDTO.getIntroduce());
        projectEntity.setArticle(projectDTO.getArticle());
        projectEntity.setLink(projectDTO.getLink());
        projectEntity.setWriter(projectDTO.getWriter());

        return projectEntity;
    }

}
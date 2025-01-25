package site.sequence.projectservice.dto;

import lombok.Builder;
import lombok.Getter;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Period;
import site.sequence.projectservice.enums.Step;

import java.util.List;

@Getter
@Builder
public class ProjectDTO {

    private String title;
    private Period period;
    private Category category;
    private Integer personnel;
    private String roles;
    private String skills;
    private MeetingOption meeting_option;
    private Step step;
    private String members;
    private String introduce;
    private String article;
    private String link;
    private String writer;

}

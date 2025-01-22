package site.sequence.projectservice.dto;

import java.time.Period;
import lombok.Builder;
import lombok.Getter;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Step;

@Getter
@Builder
public class ProjectIDTO {

    private String title;
    private Period period;
    private Category category;
    private int personnel;
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

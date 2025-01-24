package site.sequence.projectservice.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Period;
import site.sequence.projectservice.enums.Step;

@Getter
@Builder
public class ProjectDTO {

    private String title;
    private Period period;
    private Category category;
    private int personnel;
    private List<String> roles;
    private List<String> skills;
    private MeetingOption meeting_option;
    private Step step;
    private List<String> members;
    private String introduce;
    private String article;
    private String link;
    private String writer;

}

package site.sequence.projectservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.sequence.projectservice.dto.ProjectDTO;
import site.sequence.projectservice.entity.ProjectEntity;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Period;
import site.sequence.projectservice.enums.Step;
import site.sequence.projectservice.mapper.PeriodMapper;
import site.sequence.projectservice.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    public void createProject(){

    }

    public void save(ProjectDTO projectDTO){
        projectRepository.save(ProjectEntity.toProjectEntity(projectDTO));
    }

    //키워드에 해당하는 프로젝트들을 필터링
    public List<ProjectEntity> getProjectsByKeywords(Category category,
                                                     String periodKey,
                                                     String roles,
                                                     String skills,
                                                     MeetingOption meetingOption,
                                                     Step step){
        Period period = PeriodMapper.PeriodCheck(periodKey);
        return projectRepository.findProjectsByFilteredKeywords(category,period,roles,skills,meetingOption,step);
    }

    public List<ProjectEntity> getProjectsBySearch(String title){
        return projectRepository.findProjectsByFilterdSearch(title);
    }

    public List<ProjectEntity> getAllProjects(){
        return projectRepository.findAll();
    }

}

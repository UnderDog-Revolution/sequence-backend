package site.sequence.projectservice.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.sequence.projectservice.dto.ProjectDTO;
import site.sequence.projectservice.entity.ProjectEntity;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Step;
import site.sequence.projectservice.response.ApiResponseData;
import site.sequence.projectservice.response.ApiResponseMessage;
import site.sequence.projectservice.service.ProjectService;

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ProjectController {
    @Autowired
    private final ProjectService projectService;

    @PostMapping("/api/project/create")
    public ApiResponseMessage create(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ApiResponseMessage.of("프로젝트가 성공적으로 등록되었습니다.");
    }

    @GetMapping("/api/project/filter/keyword")
    public ApiResponseData<List<ProjectEntity>> filterKeyword(@RequestParam(required = false) Category category,
                                                              @RequestParam(required = false) String periodKey,
                                                              @RequestParam(required = false) String roles,
                                                              @RequestParam(required = false) String skills,
                                                              @RequestParam(required = false) MeetingOption meetingOption,
                                                              @RequestParam(required = false) Step step){
        List<ProjectEntity> projectEntities = new ArrayList<>(projectService.getProjectsByKeywords(category,periodKey,roles,skills,meetingOption,step));

        //조회된 프로젝트가 하나도 없는 경우
        if(projectEntities.isEmpty()){
            return ApiResponseData.of(projectEntities,"해당 필터와 일치하는 프로젝트가 없습니다.");
        }

        return ApiResponseData.of(projectEntities,"프로젝트 조회가 완료되었습니다.");
    }

    @GetMapping("/api/project/filter/search")
    public ApiResponseData<List<ProjectEntity>> filterSearch(@RequestParam String title){
        List<ProjectEntity> projectEntities = new ArrayList<>(projectService.getProjectsBySearch(title));
        if(projectEntities.isEmpty()){
            return ApiResponseData.of(projectEntities, "검색어와 일치하는 프로젝트가 없습니다.");
        }

        return ApiResponseData.of(projectEntities, "프로젝트 조회가 완료되었습니다");
    }

    @GetMapping("/api/project/list")
    public ApiResponseData<List<ProjectEntity>> findProjects(){
        List<ProjectEntity> projectEntities = new ArrayList<>(projectService.getAllProjects());

        if(projectEntities.isEmpty()){
            return ApiResponseData.of(projectEntities, "조회된 프로젝트가 없습니다.");
        }

        return ApiResponseData.of(projectEntities, "모든 프로젝트 조회가 완료되었습니다.");
    }

}

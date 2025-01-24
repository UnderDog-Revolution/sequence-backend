package site.sequence.projectservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.sequence.projectservice.dto.ProjectDTO;
import site.sequence.projectservice.dto.ProjectInputDTO;
import site.sequence.projectservice.entity.ProjectEntity;
import site.sequence.projectservice.entity.ProjectInvitedMemberEntity;
import site.sequence.projectservice.entity.ProjectMemberEntity;
import site.sequence.projectservice.exception.ProjectNotFoundException;
import site.sequence.projectservice.repository.ProjectInvitedMemberEntityRepository;
import site.sequence.projectservice.repository.ProjectMemberEntityRepository;
import site.sequence.projectservice.repository.ProjectRepository;
import site.sequence.projectservice.response.ApiResponse;
import site.sequence.projectservice.response.ApiResponseData;
import site.sequence.projectservice.response.ApiResponseError;
import site.sequence.projectservice.response.ApiResponseMessage;
import site.sequence.projectservice.response.Code;
import site.sequence.projectservice.utils.DataConvertor;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectInvitedMemberEntityRepository projectInvitedMemberEntityRepository;
    private final ProjectMemberEntityRepository projectMemberEntityRepository;

    // Project를 생성하는 메인 로직 함수
    @Transactional
    public ApiResponse createProject(ProjectInputDTO projectInputDTO){
        ProjectEntity project = saveProjectEntity(projectInputDTO);
        projectRepository.save(project);
        saveProjectInvitedMemberEntities(project,projectInputDTO.getInvitedMembers());
        savePrjectMemberEntity(project,projectInputDTO.getWriter());
        return ApiResponseMessage.of("정상적으로 등록되었습니다.");
    }


    // Project를 조회하는 메인 로직 함수
    @Transactional(readOnly = true)
    public ApiResponse getProject(long projectId){
        Optional<ProjectEntity> projectEntityById = projectRepository.getProjectEntityById(projectId);

        try {
            if (projectEntityById.isEmpty()) {
                throw new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다.");
            }
            return ApiResponseData.of(getProjectDTO(projectEntityById.get()),"Project를 정상적으로 조회하였습니다.");
        }
        catch (ProjectNotFoundException e){
            return ApiResponseError.of(Code.CAN_NOT_FIND_RESOURCE); //프로젝트가 존재하지 않을경우.
        }


    }

    /**
     * // 빌더패턴을 이용하여 Project를 생성하며 저장한다. 생성된 ProjectEntity는 return 한다.
     * @param projectInputDTO
     * @return ProjectEntity
     */
    public ProjectEntity saveProjectEntity(ProjectInputDTO projectInputDTO){
        return projectRepository.save(ProjectEntity.builder()
                .title(projectInputDTO.getTitle())
                .period(projectInputDTO.getPeriod())
                .category(projectInputDTO.getCategory())
                .personnel(projectInputDTO.getPersonnel())
                .roles(DataConvertor.listToString(projectInputDTO.getRoles()))
                .skills(DataConvertor.listToString(projectInputDTO.getSkills()))
                .meetingOption(projectInputDTO.getMeetingOption())
                .step(projectInputDTO.getStep())
                .introduce(projectInputDTO.getIntroduce())
                .article(projectInputDTO.getArticle())
                .link(projectInputDTO.getLink())
                .writer(projectInputDTO.getWriter())
                .build());

    }

    public ProjectDTO getProjectDTO(ProjectEntity project){
        return ProjectDTO.builder()
                .title(project.getTitle())
                .period(project.getPeriod())
                .category(project.getCategory())
                .personnel(project.getPersonnel())
                .roles(DataConvertor.stringToList(project.getRoles()))
                .skills(DataConvertor.stringToList(project.getSkills()))
                .meeting_option(project.getMeetingOption())
                .step(project.getStep())
                .members(project.getMemberUsernames())
                .introduce(project.getIntroduce())
                .article(project.getArticle())
                .link(project.getLink())
                .writer(project.getWriter())
                .build();
    }

    public void saveProjectInvitedMemberEntities(ProjectEntity project, List<String> invitedMembers){
        for(String member : invitedMembers){
            ProjectInvitedMemberEntity entity = ProjectInvitedMemberEntity.builder()
                    .username(member)
                    .project(project)
                    .build();
            projectInvitedMemberEntityRepository.save(entity);
        }
    }

    public void savePrjectMemberEntity(ProjectEntity project, String writer){
        ProjectMemberEntity entity = ProjectMemberEntity.builder()
                .username(writer)
                .project(project)
                .build();
        projectMemberEntityRepository.save(entity);
    }
}

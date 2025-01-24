package site.sequence.projectservice.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.sequence.projectservice.dto.ProjectInputDTO;
import site.sequence.projectservice.response.ApiResponse;
import site.sequence.projectservice.response.ApiResponseError;
import site.sequence.projectservice.response.Code;
import site.sequence.projectservice.service.ProjectService;

@RestController
@RequestMapping("/project-service/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse> registerProject(@Valid @RequestBody ProjectInputDTO projectInputDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(ApiResponseError.of(Code.VALIDATION_ERROR,error.getDefaultMessage()), Code.VALIDATION_ERROR.getStatus());
            }
        }
        return ResponseEntity.ok(projectService.createProject(projectInputDTO));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse> getProject(@PathVariable Integer projectId){
        ApiResponse response = projectService.getProject(projectId);
        return ResponseEntity.status(Code.getHttpStatusByCode(response.getCode())).body(response);
    }
}

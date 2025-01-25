package site.sequence.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.sequence.projectservice.entity.ProjectEntity;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Period;
import site.sequence.projectservice.enums.Step;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {

    //키워드 필터링
    @Query("SELECT p FROM ProjectEntity p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:period IS NULL OR p.period = :period) AND " +
            "(:roles IS NULL OR p.roles LIKE CONCAT('%', :roles , '%')) AND " +
            "(:skills IS NULL OR p.skills LIKE CONCAT('%', :skills, '%')) AND " +
            "(:meeting_option IS NULL OR p.meeting_option = :meeting_option) AND " +
            "(:step IS NULL OR p.step = :step)"
    )
    List<ProjectEntity> findProjectsByFilteredKeywords(
            @Param("category") Category category,
            @Param("period") Period period,
            @Param("roles") String roles,
            @Param("skills") String skills,
            @Param("meeting_option") MeetingOption meeting_option,
            @Param("step") Step step
    );

    //검색 필터링
    @Query("SELECT q From ProjectEntity q WHERE " + "(q.title LIKE CONCAT('%', :title, '%'))")
    List<ProjectEntity> findProjectsByFilterdSearch(@Param("title") String title);

}
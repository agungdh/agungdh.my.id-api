/* (C)2025 */
package id.my.agungdh.api.controller;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ProjectController {
  private ProjectService projectService;

  @QueryMapping
  public List<ProjectDTO> getProjects() {
    return projectService.findAll();
  }

  @QueryMapping
  public ProjectDTO getProject(@Argument UUID id) {
    return projectService.getProject(id);
  }

  @MutationMapping
  public ProjectDTO upsertProject(@Valid @Argument ProjectDTO input) {
    return projectService.upsertProject(input);
  }

  @MutationMapping
  public ProjectDTO deleteProject(@Argument UUID id) {
    projectService.deleteProject(id);

    return null;
  }
}

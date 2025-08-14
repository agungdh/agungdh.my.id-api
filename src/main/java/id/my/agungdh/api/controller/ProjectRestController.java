/* (C)2025 */
package id.my.agungdh.api.controller;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.mapper.ProjectMapper;
import id.my.agungdh.api.repository.ProjectRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectRestController {
  private ProjectRepository projectRepository;
  private ProjectMapper projectMapper;

  @GetMapping
  public List<Project> findAll() {
    return projectRepository.findAll();
  }

  @GetMapping("/create")
  public ProjectDTO create() {
    Project project = new Project();
    project.setName("Surimbim");
    project.setDescription("Surimbim dududuuw...");
    project.setReleaseDate(LocalDate.of(2020, Month.DECEMBER, 1));
    projectRepository.save(project);

    ProjectDTO projectDTO = projectMapper.toDTO(project);

    System.out.println(project);
    System.out.println(projectDTO);

    return projectDTO;
  }
}

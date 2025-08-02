package id.my.agungdh.api.controller;

import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectRestController {
    private ProjectRepository projectRepository;

    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @GetMapping("/create")
    public Project create() {
        Project project = new Project();
        project.setName("Surimbim");
        project.setDescription("Surimbim dududuuw...");
        project.setReleaseDate(LocalDate.of(2020, Month.DECEMBER, 1));
        projectRepository.save(project);
        return project;
    }
}

package id.my.agungdh.api.controller;

import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {
    private ProjectRepository projectRepository;

    @GetMapping("/")
    public List<Project> findAll() {
        return projectRepository.findAll();
    }
}

package id.my.agungdh.api.controller;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.input.UpsertProjectInput;
import id.my.agungdh.api.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class ProjectController {
    private ProjectService projectService;

    @QueryMapping
    public List<Project> projects() {
        return projectService.findAll();
    }

    @QueryMapping
    public Project projectById(@Argument UUID id) {
        return projectService.findById(id);
    }

    @MutationMapping
    public ProjectDTO upsertProject(@Argument ProjectDTO input) {
        System.out.println(input);
        return projectService.upsertProject(new ProjectDTO(
                input.id(),
                input.name(),
                input.description(),
                input.releaseDate()
        ));
    }

    @MutationMapping
    public Boolean deleteProject(@Argument String id) {
        return projectService.deleteProject(id);
    }
}

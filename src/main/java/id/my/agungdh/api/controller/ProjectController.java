package id.my.agungdh.api.controller;

import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

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
}

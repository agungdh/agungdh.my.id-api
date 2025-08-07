package id.my.agungdh.api.service;

import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.input.UpsertProjectInput;
import id.my.agungdh.api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService {
    private ProjectRepository projectRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(UUID id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project upsertProject(UpsertProjectInput input) {
        Project project;
        System.out.println(input);
        if (input.id() != null) {
            // update existing
            project = projectRepository.findById(UUID.fromString(input.id())).orElseThrow();
            System.out.println("update");
        } else {
            // create new
            project = new Project();
            System.out.println("create");
        }

        System.out.println(project);

        // copy all non-null props from input → project
        BeanUtils.copyProperties(input, project, getNullPropertyNames(input));

        // save/overwrite
        projectRepository.save(project);
        return project;
    }

    // helper untuk abaikan field null
    private String[] getNullPropertyNames(UpsertProjectInput src) {
        var props = new ArrayList<String>();
        if (src.id() == null) props.add("id");
        if (src.name() == null) props.add("name");
        if (src.description() == null) props.add("description");
        if (src.releaseDate() == null) props.add("releaseDate");
        return props.toArray(String[]::new);
    }

    public boolean deleteProject(String id) {
        projectRepository.deleteById(UUID.fromString(id));

        return true;
    }
}

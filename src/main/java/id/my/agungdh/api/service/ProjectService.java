package id.my.agungdh.api.service;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.mapper.ProjectMapper;
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
    private ProjectMapper projectMapper;

    public List<ProjectDTO> findAll() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for (Project project : projects) {
            projectDTOs.add(
                    projectMapper.toDTO(project)
            );
        }

        return projectDTOs;
    }

    public Project find(UUID id) {
        return projectRepository.findByUuid(id).orElseThrow();
    }

    public ProjectDTO getProject(UUID id) {
        return projectMapper.toDTO(find(id));
    }

    public ProjectDTO upsertProject(ProjectDTO projectDto) {
        System.out.println("Project: " + projectDto);

        Project project;

        if (projectDto.id() != null) {
            project = find(projectDto.id());
        } else {
            project = new Project();
        }

        System.out.println(project);

        // copy all non-null props from projectDto → project
        BeanUtils.copyProperties(projectDto, project, getNullPropertyNames(projectDto));

        // save/overwrite
        projectRepository.save(project);

        return projectMapper.toDTO(project);
    }

    // helper untuk abaikan field null
    private String[] getNullPropertyNames(ProjectDTO src) {
        var props = new ArrayList<String>();
        if (src.id() == null) props.add("id");
        if (src.name() == null) props.add("name");
        if (src.description() == null) props.add("description");
        if (src.releaseDate() == null) props.add("releaseDate");
        return props.toArray(String[]::new);
    }

    public void deleteProject(UUID id) {
        Project project = find(id);

        projectRepository.delete(project);
    }
}

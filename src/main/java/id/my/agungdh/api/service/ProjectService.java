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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {
        return projectRepository.findAll().stream().map(projectMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Project find(UUID id) {
        return projectRepository.findByUuid(id).orElseThrow(); // tambahkan NotFound exception sesuai selera
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProject(UUID id) {
        return projectMapper.toDTO(find(id));
    }

    @Transactional
    public ProjectDTO upsertProject(ProjectDTO dto) {
        Project entity;
        if (dto.id() == null) {
            // CREATE
            entity = projectMapper.toEntity(dto); // uuid akan diisi dari dto.id (null) -> @PrePersist bikin UUID baru
        } else {
            // UPDATE
            entity = find(dto.id());              // load managed entity
            projectMapper.updateEntity(dto, entity); // patch field yang boleh diubah
        }
        entity = projectRepository.save(entity);
        return projectMapper.toDTO(entity);
    }

    @Transactional
    public void deleteProject(UUID id) {
        projectRepository.delete(find(id));
    }
}

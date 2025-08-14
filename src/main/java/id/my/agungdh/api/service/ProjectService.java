/* (C) 2025 Agung DH */
package id.my.agungdh.api.service;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.entity.Project;
import id.my.agungdh.api.mapper.ProjectMapper;
import id.my.agungdh.api.repository.ProjectRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
    return projectRepository.findByUuid(id).orElseThrow();
  }

  @Transactional(readOnly = true)
  public ProjectDTO getProject(UUID id) {
    return projectMapper.toDTO(find(id));
  }

  @Transactional
  public ProjectDTO upsertProject(ProjectDTO dto) {
    Project entity;
    if (dto.id() == null) {
      entity = projectMapper.toEntity(dto);
    } else {
      entity = find(dto.id());
      projectMapper.updateEntity(dto, entity);
    }
    entity = projectRepository.save(entity);
    return projectMapper.toDTO(entity);
  }

  @Transactional
  public void deleteProject(UUID id) {
    projectRepository.delete(find(id));
  }
}

package id.my.agungdh.api.mapper;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "uuid", target = "id")
    ProjectDTO toDTO(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "uuid")
    Project toEntity(ProjectDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ProjectDTO dto, @MappingTarget Project entity);
}

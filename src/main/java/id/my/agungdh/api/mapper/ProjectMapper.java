package id.my.agungdh.api.mapper;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "releaseDate", target = "releaseDate")
    ProjectDTO toDTO(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "uuid")
    Project toEntity(ProjectDTO projectDTO);
}

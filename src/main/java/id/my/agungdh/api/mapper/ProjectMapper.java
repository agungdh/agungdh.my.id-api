package id.my.agungdh.api.mapper;

import id.my.agungdh.api.dto.ProjectDTO;
import id.my.agungdh.api.entity.Project;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    // Entity -> DTO
    @Mapping(source = "uuid", target = "id")
    ProjectDTO toDTO(Project project);

    // DTO -> Entity (buat CREATE). id(Long) autogen di-ignore, UUID diambil dari DTO.id
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "uuid")
    Project toEntity(ProjectDTO dto);

    // DTO -> existing Entity (buat UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)        // jangan sentuh PK Long
    @Mapping(target = "uuid", ignore = true)      // jangan ubah UUID
    @Mapping(target = "createdAt", ignore = true) // auditing
    @Mapping(target = "updatedAt", ignore = true) // auditing
    void updateEntity(ProjectDTO dto, @MappingTarget Project entity);
}

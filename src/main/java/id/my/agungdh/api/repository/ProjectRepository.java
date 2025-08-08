package id.my.agungdh.api.repository;

import id.my.agungdh.api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByUuid(UUID uuid);
}

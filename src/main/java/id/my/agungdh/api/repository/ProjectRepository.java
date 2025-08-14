/* (C)2025 */
package id.my.agungdh.api.repository;

import id.my.agungdh.api.entity.Project;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  Optional<Project> findByUuid(UUID uuid);
}

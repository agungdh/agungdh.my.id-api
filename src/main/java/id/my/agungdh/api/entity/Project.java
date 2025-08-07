package id.my.agungdh.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(
        indexes = @Index(name = "idx_uuid_v4", columnList = "uuid")
        // WIP: dibuat hash index
)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String description;

    private LocalDate releaseDate;

    @PrePersist
    public void onPrePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}

package id.my.agungdh.api.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Project {
    @Id
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String description;
    private LocalDate releaseDate;

    @PrePersist
    public void onPrePersist() {
        if (id == null) {
            // generate UUIDv7
            id = UuidCreator.getTimeOrdered();
        }
    }
}

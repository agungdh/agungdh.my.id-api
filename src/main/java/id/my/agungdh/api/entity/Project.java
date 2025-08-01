package id.my.agungdh.api.entity;

import jakarta.persistence.Id;
import jdk.jfr.Enabled;
import lombok.Data;

import java.time.LocalDate;

@Enabled
@Data
public class Project {
    @Id
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
}

package id.my.agungdh.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectDTO(
        UUID id,
        String name,
        String description,
        LocalDate releaseDate
) {
}

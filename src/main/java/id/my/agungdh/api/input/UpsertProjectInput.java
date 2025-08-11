package id.my.agungdh.api.input;

import java.time.LocalDate;
import java.util.UUID;

public record UpsertProjectInput(
        UUID id,
        String name,
        String description,
        LocalDate releaseDate
) {
}

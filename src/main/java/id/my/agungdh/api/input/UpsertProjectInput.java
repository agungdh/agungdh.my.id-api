package id.my.agungdh.api.input;

import java.util.UUID;

public record UpsertProjectInput(
        String id,
        String name,
        String description,
        String releaseDate
) {
}

package id.my.agungdh.api.input;

public record UpsertProjectInput(
        String id,
        String name,
        String description,
        String releaseDate
) {
}

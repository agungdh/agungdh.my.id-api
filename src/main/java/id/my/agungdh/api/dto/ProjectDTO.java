/* (C) 2025 Agung DH */
package id.my.agungdh.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectDTO(
    UUID id,
    @NotBlank String name,
    @NotBlank @NotNull @NotEmpty String description,
    LocalDate releaseDate) {}

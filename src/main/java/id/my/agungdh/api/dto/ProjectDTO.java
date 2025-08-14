/* (C)2025 */
package id.my.agungdh.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectDTO(
    UUID id, @NotBlank String name, @NotBlank String description, LocalDate releaseDate) {}

package dev.nicolas.startuprush.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StartupDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Slogan is required")
    private String slogan;

    @Min(value = 1800, message = "Foundation year must be after 1800")
    // TODO: Update the @max to the current year dynamically
    @Max(value = 2025, message = "Foundation year must be 2025 or earlier")
    private int foundationYear;
}

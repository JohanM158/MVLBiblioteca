package com.vml.biblioteca.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 200, message = "El autor no puede exceder 200 caracteres")
    private String author;

    @Size(max = 20, message = "El ISBN no puede exceder 20 caracteres")
    private String isbn;

    @Size(max = 100, message = "El género no puede exceder 100 caracteres")
    private String genre;

    @Min(value = 1000, message = "El año debe ser al menos 1000")
    @Max(value = 2100, message = "El año no puede ser mayor a 2100")
    private Integer year;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    private Boolean available;
    private String createdAt;
    private String updatedAt;
}

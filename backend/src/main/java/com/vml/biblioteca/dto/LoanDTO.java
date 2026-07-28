package com.vml.biblioteca.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private Long id;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long bookId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    // Read-only fields populated by the backend
    private String bookTitle;
    private String bookAuthor;
    private String userName;
    private String userEmail;

    private String loanDate;
    private String dueDate;
    private String returnDate;
    private String status;

    @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
    private String notes;
}

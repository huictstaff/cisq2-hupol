package nl.hu.cisq2.hupol.security.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}

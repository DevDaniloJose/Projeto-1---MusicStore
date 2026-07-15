package com.musicStore.api_loja_discos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignUpRequest(
        @NotBlank
        @Size(min = 3)
        String username,
        @NotBlank
        @Size(min = 6)
        String password
) {
}

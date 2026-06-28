package com.musicStore.api_loja_discos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull
        String username,
        @NotBlank
        @NotNull
        String password) {
}

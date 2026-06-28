package com.musicStore.api_loja_discos.requests;

import java.time.LocalDateTime;

public record ErrorResponseDTO(int status, String message, String error, LocalDateTime stamp) {
}

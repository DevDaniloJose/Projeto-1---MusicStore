package com.musicStore.api_loja_discos.exceptions;

public class InvalidReleaseYearException extends RuntimeException {
    public InvalidReleaseYearException(String message) {
        super(message);
    }
}

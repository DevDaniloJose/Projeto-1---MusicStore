package com.musicStore.api_loja_discos.exceptions;

import lombok.Getter;

import java.util.List;

@Getter

public class InvalidArtistDataException extends RuntimeException {

    private final List<String> fields;
    private final List<String> fieldsMessage;


    public InvalidArtistDataException(String message, List<String> fields, List<String> fieldsMessage) {
        super(message);
        this.fields = fields;
        this.fieldsMessage = fieldsMessage;
    }
}

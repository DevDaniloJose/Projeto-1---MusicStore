package com.musicStore.api_loja_discos.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class InvalidReleaseYearExceptionDetails extends ExceptionDetails {

    private final List<String> fieldsError;
    private final List<String> fieldsMessage;

}

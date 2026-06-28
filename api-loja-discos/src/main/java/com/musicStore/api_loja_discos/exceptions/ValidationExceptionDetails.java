package com.musicStore.api_loja_discos.exceptions;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {

    private final List<String> fieldsError;
    private final List<String> fieldsMessage;
}


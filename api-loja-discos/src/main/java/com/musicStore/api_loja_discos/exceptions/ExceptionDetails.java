package com.musicStore.api_loja_discos.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Data
@SuperBuilder
public class ExceptionDetails {

     protected final String title;
     protected final int status;
     protected final String details;
     protected final String developerMessage;
     protected final LocalDateTime timestamp;

}

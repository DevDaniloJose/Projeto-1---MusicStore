package com.musicStore.api_loja_discos.exceptions;



public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}

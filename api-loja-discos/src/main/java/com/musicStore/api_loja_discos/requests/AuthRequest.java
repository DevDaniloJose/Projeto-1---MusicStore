package com.musicStore.api_loja_discos.requests;

//Quando o usuário vai na tela de login, digita o nome e a senha e clica no botão "Entrar", o front-end empacota esses dados e envia para a sua API.
//
//Como esses dados estão entrando na sua API como um pedido, eles são a REQUISIÇÃO.
//
//O seu record AuthRequest está perfeito para isso. Ele é o Payload de Requisição. Ele serve para o Java receber o que veio da rua.

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "Name and password cannot be blank")
        String username,
        String password




) {


    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}

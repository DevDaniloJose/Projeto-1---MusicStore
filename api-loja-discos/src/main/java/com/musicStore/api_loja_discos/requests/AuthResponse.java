package com.musicStore.api_loja_discos.requests;
//Depois que o seu código Java recebe o AuthRequest, ele confere no banco se a senha está certa. Se estiver tudo OK,
// a sua API precisa responder para o front-end entregando os tokens de acesso.
//
//Como esses dados estão saindo da sua API de volta para o navegador do usuário, eles são a RESPOSTA.
//
//Para isso, você cria o irmão dele, o AuthResponse (ou LoginResponseDTO).
public record AuthResponse(
        String token,
        com.musicStore.api_loja_discos.domain.RefreshToken refreshToken,
        String message
) {
}

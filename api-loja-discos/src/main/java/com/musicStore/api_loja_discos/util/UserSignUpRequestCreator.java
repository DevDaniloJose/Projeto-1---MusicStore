package com.musicStore.api_loja_discos.util;

import com.musicStore.api_loja_discos.requests.SignUpRequest;
import com.musicStore.api_loja_discos.requests.SignUpResponse;

public class UserSignUpRequestCreator {

    public static SignUpRequest createSignUpRequest() {
       return SignUpRequest.builder()
                .username("Masayoshi_takanaka")
                .password("123")
                .build();
    }

}

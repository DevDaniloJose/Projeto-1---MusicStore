package com.musicStore.api_loja_discos.util;

import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.ArtistPostRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistPutRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistSignUpRequest;

public class ArtistCreator {

    public static Artist createArtistToBeSaved() {
        return Artist.builder()
                .stageName("Masayoshi takanaka")
                .genre("Japanese jazz")
                .build();
    }

    public static ArtistSignUpRequest createArtistPostRequestBody() {
      return  ArtistSignUpRequest.builder()
              .username("Masayoshi_takanaka")
              .stageName("Masayoshi Takanaka")
              .password("password")
              .genre("City Pop")
              .bio("Nice guitarist")
              .country("country")
              .build();
    }


        public static ArtistPutRequestBody createArtistPutRequestBody() {
        return ArtistPutRequestBody.builder()
                .id(1L)
                .stageName("Masayoshi Takanaka")
                .username("Masayoshi_takanaka")
                .genre("Japanese jazz")
                .build();
        }

        //saving
        public static ArtistDTO createArtistDTO() {
            return ArtistDTO.builder()
                    .username("Masayoshi_Username")
                    .stageName("Masayoshi Takanaka")
                    .genre("Japanese jazz")
                    .build();
        }

        //Para testes de buscas e listagens (simular oq eu ja tenho no banco)

    public static ArtistDTO createValidArtistDTO() {
        return ArtistDTO.builder()
                .id(1L)
                .username("Masayoshi_takanaka")
                .stageName("Masayoshi Takanaka")
                .genre("Japanese jazz")
                .build();
    }


    public static Artist createValidArtist() {
        return Artist.builder()
                .id(1L)
                .stageName("Masayoshi takanaka")
                .genre("Japanese jazz")
                .build();
    }


    public static Artist createValidArtistNoId() {
        return Artist.builder()
                .stageName("Masayoshi takanaka")
                .genre("Japanese jazz")
                .build();
    }
    public static Artist createValidUpdatedArtist() {
        return Artist.builder()
                .id(1L)
                .stageName("Masayoshi Takanaka")
                .genre("Japanese jazz")
                .build();
    }

}

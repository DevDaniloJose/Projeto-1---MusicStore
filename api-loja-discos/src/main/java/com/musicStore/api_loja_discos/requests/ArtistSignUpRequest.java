package com.musicStore.api_loja_discos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ArtistSignUpRequest(@NotBlank
                                  String username,
                                  @NotBlank
                                  String password,
                                  @NotBlank
                                  String stageName,
                                  @NotBlank
                                  String bio,
                                  @NotBlank
                                  String genre,
                                  @NotBlank
                                 String country)
{
}

package com.musicStore.api_loja_discos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistPutRequestBody {

    @NotNull
    private Long id;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z\\s]+", message = "Artist name must not contain numbers")
    private String username;

    @NotBlank
    private String stageName;

    @NotBlank(message = "Genre is mandatory")
    private String genre;
}

package com.musicStore.api_loja_discos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @NotNull
    @Pattern(regexp = "[a-zA-Z\\s]+", message = "Artist name must not contain numbers")
    private String username;
    @NotBlank(message = "Genre is mandatory")
    private String genre;
    @NotBlank(message = "stage name is mandatory")
    private String stageName;

    private String bio;
    @NotBlank(message = "Country is mandatory")
    private String country;





}

package com.musicStore.api_loja_discos.requests;

import com.musicStore.api_loja_discos.domain.Album;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDTO {

    private String title;
    @Min(1900)
    private Integer releaseYear;
    private Integer durationMinutes;
    private Long id;
    private ArtistDTO artist;
}

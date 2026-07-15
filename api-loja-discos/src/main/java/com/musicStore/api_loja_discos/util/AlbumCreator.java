package com.musicStore.api_loja_discos.util;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.requests.AlbumDTO;
import com.musicStore.api_loja_discos.requests.ArtistDTO;

public class AlbumCreator {

    public static Album createAlbumToBeSaved(Artist artist) {
        return Album.builder()
                .title("Brasillian Skies")
                .artist(artist)
                .durationMinutes(47)
                .releaseYear(1978)
                .build();
    }

    public static AlbumDTO createAlbumDTO(ArtistDTO artist) {
        return AlbumDTO.builder()
                .title("Brasillian Skies")
                .artist(artist)
                .durationMinutes(47)
                .releaseYear(1978)
                .build();
    }

}

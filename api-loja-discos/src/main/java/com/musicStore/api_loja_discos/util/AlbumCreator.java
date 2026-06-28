package com.musicStore.api_loja_discos.util;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;

public class AlbumCreator {

    public static Album createAlbumToBeSaved(Artist artist) {
        return Album.builder()
                .title("Brasillian Skies")
                .artist(artist)
                .durationMinutes(47)
                .releaseYear(1978)
                .build();
    }

}

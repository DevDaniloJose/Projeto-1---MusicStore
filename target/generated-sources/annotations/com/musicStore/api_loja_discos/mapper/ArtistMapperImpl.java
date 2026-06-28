package com.musicStore.api_loja_discos.mapper;

import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.ArtistPostRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistPutRequestBody;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-30T16:50:27-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class ArtistMapperImpl extends ArtistMapper {

    @Override
    public ArtistDTO toArtistDTO(Artist artist) {
        if ( artist == null ) {
            return null;
        }

        ArtistDTO.ArtistDTOBuilder artistDTO = ArtistDTO.builder();

        artistDTO.id( artist.getId() );
        artistDTO.name( artist.getName() );
        artistDTO.genre( artist.getGenre() );

        return artistDTO.build();
    }

    @Override
    public Artist toArtist(ArtistPostRequestBody artistPostRequestBody) {
        if ( artistPostRequestBody == null ) {
            return null;
        }

        Artist.ArtistBuilder artist = Artist.builder();

        artist.name( artistPostRequestBody.getName() );
        artist.genre( artistPostRequestBody.getGenre() );

        return artist.build();
    }

    @Override
    public Artist toArtist(ArtistPutRequestBody artistPutRequestBody) {
        if ( artistPutRequestBody == null ) {
            return null;
        }

        Artist.ArtistBuilder artist = Artist.builder();

        artist.id( artistPutRequestBody.getId() );
        artist.name( artistPutRequestBody.getName() );
        artist.genre( artistPutRequestBody.getGenre() );

        return artist.build();
    }

    @Override
    public Artist toArtist(ArtistDTO artistDTO) {
        if ( artistDTO == null ) {
            return null;
        }

        Artist.ArtistBuilder artist = Artist.builder();

        artist.id( artistDTO.getId() );
        artist.name( artistDTO.getName() );
        artist.genre( artistDTO.getGenre() );

        return artist.build();
    }
}

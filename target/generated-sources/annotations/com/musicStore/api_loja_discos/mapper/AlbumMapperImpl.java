package com.musicStore.api_loja_discos.mapper;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.requests.AlbumDTO;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-30T16:50:27-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class AlbumMapperImpl extends AlbumMapper {

    @Override
    public Album toAlbum(AlbumDTO albumDTO) {
        if ( albumDTO == null ) {
            return null;
        }

        Album.AlbumBuilder album = Album.builder();

        album.artist( artistDTOToArtist( albumDTO.getArtist() ) );
        album.id( albumDTO.getId() );
        album.releaseYear( albumDTO.getReleaseYear() );
        album.durationMinutes( albumDTO.getDurationMinutes() );
        album.title( albumDTO.getTitle() );

        return album.build();
    }

    @Override
    public AlbumDTO toAlbumDTO(Album album) {
        if ( album == null ) {
            return null;
        }

        AlbumDTO albumDTO = new AlbumDTO();

        albumDTO.setTitle( album.getTitle() );
        albumDTO.setReleaseYear( album.getReleaseYear() );
        albumDTO.setDurationMinutes( album.getDurationMinutes() );
        albumDTO.setId( album.getId() );
        albumDTO.setArtist( artistToArtistDTO( album.getArtist() ) );

        return albumDTO;
    }

    protected Artist artistDTOToArtist(ArtistDTO artistDTO) {
        if ( artistDTO == null ) {
            return null;
        }

        Artist.ArtistBuilder artist = Artist.builder();

        artist.id( artistDTO.getId() );
        artist.name( artistDTO.getName() );
        artist.genre( artistDTO.getGenre() );

        return artist.build();
    }

    protected ArtistDTO artistToArtistDTO(Artist artist) {
        if ( artist == null ) {
            return null;
        }

        ArtistDTO.ArtistDTOBuilder artistDTO = ArtistDTO.builder();

        artistDTO.id( artist.getId() );
        artistDTO.name( artist.getName() );
        artistDTO.genre( artist.getGenre() );

        return artistDTO.build();
    }
}

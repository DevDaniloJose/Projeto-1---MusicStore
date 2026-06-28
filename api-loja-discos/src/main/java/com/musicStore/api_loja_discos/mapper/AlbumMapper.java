package com.musicStore.api_loja_discos.mapper;


import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.requests.AlbumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AlbumMapper {

    @Mapping(target = "artist", source = "artist")
    public abstract Album toAlbum(AlbumDTO albumDTO);

    public abstract AlbumDTO toAlbumDTO(Album album);

}

package com.musicStore.api_loja_discos.mapper;


import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.ArtistPostRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistPutRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistSignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class ArtistMapper {

    public static final ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

    public abstract ArtistDTO toArtistDTO(Artist artist);

    public abstract Artist toArtist(ArtistPostRequestBody artistPostRequestBody);

    public abstract Artist toArtist(ArtistPutRequestBody artistPutRequestBody);

    public abstract Artist toArtist(ArtistDTO artistDTO);


    public abstract Artist toArtist(ArtistSignUpRequest artistPostRequestBody);
}

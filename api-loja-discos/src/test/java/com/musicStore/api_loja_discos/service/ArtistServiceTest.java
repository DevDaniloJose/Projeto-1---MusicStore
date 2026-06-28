package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.exceptions.BusinessRuleException;
import com.musicStore.api_loja_discos.mapper.ArtistMapper;
import com.musicStore.api_loja_discos.mapper.ArtistMapperImpl;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.ArtistPostRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistPutRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistSignUpRequest;
import com.musicStore.api_loja_discos.util.ArtistCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ArtistMapper artistMapper = new ArtistMapperImpl();

    @InjectMocks
    private ArtistService artistService;


    @Test
    @DisplayName("Should save Artist if they data are valid and return the DTO")
    void shouldSaveArtist_IfDataIsValid() {
        //Arrange
        ArtistSignUpRequest artistPostRequestBody = ArtistCreator.createArtistPostRequestBody();
        Artist artist = ArtistCreator.createValidArtist();
        ArtistDTO artistDTO = ArtistCreator.createArtistDTO();

        when(artistMapper.toArtist(artistPostRequestBody)).thenReturn(artist);
        when(artistRepository.save(artist)).thenReturn(artist);
        when(artistMapper.toArtistDTO(artist)).thenReturn(artistDTO);

        // ACT

        ArtistDTO result = artistService.save(artistPR);

        assertNotNull(result);
        assertEquals("Masayoshi takanaka", result.getName());
        verify(artistRepository, times(1)).save(artist);
    }

    @Test
    @DisplayName("Should delete an artist when they have no albums")
    void shouldDeleteAnArtist_WhenTheyHaveNoAlbums() {
        Artist artist = ArtistCreator.createValidArtist();

        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));

        // ACT

        artistService.delete(artist.getId());


        verify(artistRepository, times(1)).deleteById(artist.getId());
    }


    @Test
    @DisplayName("Should NOT delete an artist when they have an album and then throws BusinessRuleException")
    void shouldNotDeleteArtist_WhenTheyHaveAnAlbum() {
        Artist artist = ArtistCreator.createValidArtist();
        artist.setAlbums(List.of(new Album()));
        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));

        assertThrows(BusinessRuleException.class, () -> artistService.delete(artist.getId()));

        verify(artistRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should replace an artist")
    void shouldReplaceAnArtist() {
        Long id = 1L;
    ArtistPutRequestBody updatedRequest = ArtistCreator.createArtistPutRequestBody();
    Artist artistInDb = ArtistCreator.createValidArtist();
    ArtistDTO expectedDTO =  ArtistCreator.createArtistDTO();

    when(artistRepository.findById(updatedRequest.getId())).thenReturn(Optional.of(artistInDb));
    when(artistRepository.save(any(Artist.class))).thenReturn(artistInDb);
    when(artistMapper.toArtistDTO(any())).thenReturn(expectedDTO);
    
    // ACT

        ArtistDTO result = artistService.replace(updatedRequest);


        //Assert

        assertNotNull(result);

        // Verify
        verify(artistRepository, times(1)).save(any(Artist.class));
        verify(artistRepository, times(1)).findById(id);
    }


}

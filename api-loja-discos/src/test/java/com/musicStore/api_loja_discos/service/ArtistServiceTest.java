package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.exceptions.BadRequestException;
import com.musicStore.api_loja_discos.exceptions.BusinessRuleException;
import com.musicStore.api_loja_discos.exceptions.ResourceNotFoundException;
import com.musicStore.api_loja_discos.mapper.ArtistMapper;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.ArtistPostRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistPutRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistSignUpRequest;
import com.musicStore.api_loja_discos.util.ArtistCreator;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ArtistMapper artistMapper;

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

        ArtistDTO result = artistService.save(artistPostRequestBody);

        assertNotNull(result);
        assertEquals("Masayoshi Takanaka", result.getStageName());
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

    @Test
    @DisplayName("should find an artist by their stage name")
    void shouldFindArtistBy_StageName() {
        Artist artist = ArtistCreator.createValidArtist();
        List<Artist> validArtistList = ArtistCreator.createValidArtistList();
        List<ArtistDTO> validArtistDTO = ArtistCreator.createValidArtistDTOList();

        when(artistRepository.findByStageNameContainingIgnoreCase(artist.getStageName())).thenReturn(Optional.of(validArtistList));
        when(artistMapper.toArtistDTOList(validArtistList)).thenReturn(validArtistDTO);
        List<ArtistDTO> artistsWithSameName = artistService.findByStageName(artist.getStageName());

        org.assertj.core.api.Assertions.assertThat(artistsWithSameName.getFirst().getStageName()).isEqualTo(validArtistDTO.getFirst().getStageName());
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException if no artists found by stage name")
    void shouldThrowResourceNotFoundException_IfNoArtistFound() {
        String unknownName = "unknown";
        when(artistRepository.findByStageNameContainingIgnoreCase(unknownName)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> artistService.findByStageName(unknownName));
        org.assertj.core.api.Assertions.assertThat(exception.getMessage())
                .isEqualTo("no Artist found in db with that name");
    }

    @Test
    @DisplayName("should find an artist by their id")
    void shouldFindArtistById() {
        Artist artistOnDb = ArtistCreator.createValidArtist();
        ArtistDTO artistDTO = ArtistCreator.createValidArtistDTO();
        when(artistRepository.findById(artistOnDb.getId())).thenReturn(Optional.of(artistOnDb));
        when(artistMapper.toArtistDTO(artistOnDb)).thenReturn(artistDTO);
        ArtistDTO artistFound = artistService.findArtistById(artistOnDb.getId());

        org.assertj.core.api.Assertions.assertThat(Optional.of(artistFound)).isPresent();
        org.assertj.core.api.Assertions.assertThat(artistFound.getId()).isEqualTo(artistOnDb.getId());
    }

    @Test
    @DisplayName("Should throw resourceNotFoundException if not found by id")
    void shouldThrowResourceNotFoundException_IfNotFoundById() {
        Artist artistNoId = ArtistCreator.createValidArtistNoId();
        when(artistRepository.findById(artistNoId.getId())).thenReturn(Optional.empty());

            assertThrows(BadRequestException.class, () -> artistService.findArtistById(artistNoId.getId()));
    }


}

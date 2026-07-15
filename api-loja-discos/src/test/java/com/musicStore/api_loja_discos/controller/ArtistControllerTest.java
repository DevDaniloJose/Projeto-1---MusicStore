package com.musicStore.api_loja_discos.controller;

import com.musicStore.api_loja_discos.exceptions.BadRequestException;
import com.musicStore.api_loja_discos.mapper.ArtistMapper;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.ArtistPutRequestBody;
import com.musicStore.api_loja_discos.service.ArtistService;
import com.musicStore.api_loja_discos.util.ArtistCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ArtistControllerTest {

    @InjectMocks
    ArtistController artistController;

    @Mock
    ArtistService artistService;


    @Test
    @DisplayName("List returns list of artist inside page object when successful")
    void list_ReturnsListOfArtistInsidePageObjet_WhenSuccessful() {

        PageImpl<ArtistDTO> artistPage = new PageImpl<>(List.of(ArtistCreator.createArtistDTO()));

        BDDMockito.when(artistService.listArtists(ArgumentMatchers.any())).thenReturn(artistPage);

        artistController.listArtists(PageRequest.of(0, 10));

        String expectedName = ArtistCreator.createValidArtist().getStageName();
        org.assertj.core.api.Assertions.assertThat(artistPage).isNotEmpty();
       Assertions.assertEquals("Masayoshi takanaka", expectedName);
    }

    @Test
    @DisplayName("Should return an artist dto by id when found one")
    void shouldReturnAnArtist_WhenPassedId() {
        ArtistDTO expectedArtist = ArtistCreator.createArtistDTO();

       BDDMockito.given(artistService.findArtistById(any())).willReturn(expectedArtist);

        ResponseEntity<ArtistDTO> response = artistController.findByIdOrElseThrow(1L);

        org.assertj.core.api.Assertions.assertThat(response.getBody()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getBody().getStageName()).isEqualTo(expectedArtist.getStageName());
    }

    @Test
    @DisplayName("Should throw BadRequestException when artist is not found")
    void shouldTrowBadRequestException_WhenArtistNotFound() {

        BDDMockito.given(artistService.findArtistById(any())).willThrow(new BadRequestException("Artist not found"));

        org.assertj.core.api.Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> artistController.findByIdOrElseThrow(1L))
                .withMessageContaining("Artist not found");
    }

    @Test
    @DisplayName("should replace the artist")
    void shouldReplaceTheGenreOfAnArtist() {
        ArtistPutRequestBody request = ArtistCreator.createArtistPutRequestBody();
        ArtistDTO expectedResponse = ArtistCreator.createValidArtistDTO();

        BDDMockito.given(artistService.replace(ArgumentMatchers.any(ArtistPutRequestBody.class))).willReturn(expectedResponse);

        ResponseEntity<ArtistDTO> response = artistController.replace(request);

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        org.assertj.core.api.Assertions.assertThat(response.getBody().getStageName()).isEqualTo(expectedResponse.getStageName());

    }


}
package com.musicStore.api_loja_discos.integration;

import com.musicStore.api_loja_discos.Wrapper.PageableResponse;
import com.musicStore.api_loja_discos.controller.ArtistController;
import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.exceptions.BadRequestException;
import com.musicStore.api_loja_discos.mapper.ArtistMapper;
import com.musicStore.api_loja_discos.repository.AlbumRepository;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.ArtistPostRequestBody;
import com.musicStore.api_loja_discos.requests.ArtistPutRequestBody;
import com.musicStore.api_loja_discos.service.ArtistService;
import com.musicStore.api_loja_discos.util.AlbumCreator;
import com.musicStore.api_loja_discos.util.ArtistCreator;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtistControllerIT {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Container
    @ServiceConnection
    private static final MySQLContainer<?>  container = new MySQLContainer<>("mysql:8.0");

    @LocalServerPort
    private int port;

    @Autowired
    private ArtistRepository artistRepository;


    private ArtistController artistController;

    @Autowired
    private AlbumRepository albumRepository;


    @BeforeEach
    void setUp() {
        albumRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    @DisplayName("List returns list of artist inside page object when successful")
    void list_ReturnsListOfArtistInsidePageObjet_WhenSuccessful() {

        Artist saved = artistRepository.save(ArtistCreator.createArtistToBeSaved());

        PageableResponse<Artist> artistPage = testRestTemplate.exchange("/artists/list-artists", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Artist>>() {
        }).getBody();

        org.assertj.core.api.Assertions.assertThat(artistPage).isNotNull();
        org.assertj.core.api.Assertions.assertThat(artistPage.getContent()).hasSize(1);
        org.assertj.core.api.Assertions.assertThat(artistPage.getContent().get(0).getName()).isEqualTo(saved.getName());
    }



    @Test
    @DisplayName("Should return an artist dto by id when found one")
    void shouldReturnAnArtist_WhenPassedId() {

        Artist artistOnDb = artistRepository.save(ArtistCreator.createArtistToBeSaved());
        Long idEsperado = artistOnDb.getId();


        ResponseEntity<? extends Artist> response = testRestTemplate.exchange("/artists/{id}", HttpMethod.GET, null, artistOnDb.getClass(), artistOnDb.getId());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(idEsperado, artistOnDb.getId(), "Id is correct");
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(idEsperado, response.getBody().getId(), "The return id by the API must be equal as the saved one");
    }

    @Test
    @DisplayName("Should return 400 when artist is not found by id")
    void shouldReturn400_WhenArtistIsNotFoundById() {

        ResponseEntity<Void> response = testRestTemplate.exchange("/artists/{id}", HttpMethod.GET, null, Void.class, 999L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("should replace all artist fields when successful")
    void update_UpdatesAllArtistFields_WhenSuccessful() {
        Artist savedArtist = artistRepository.save(ArtistCreator.createArtistToBeSaved());

        ArtistPutRequestBody artistPutRequestBody =
                ArtistPutRequestBody.builder()
                .id(savedArtist.getId())
                .name("Tokenainamae")
                .genre("japanese shoegaze")
                        .build();


        testRestTemplate.exchange("/artists/replace/{id}", HttpMethod.PUT, new HttpEntity<>(artistPutRequestBody), Void.class, savedArtist.getId());

        Artist updatedArtist = artistRepository.findById(savedArtist.getId()).get();

        Assertions.assertAll("Verifying all fields",
                () -> Assertions.assertEquals("Tokenainamae", updatedArtist.getName(), "Name should match"),
                () -> Assertions.assertEquals("japanese shoegaze", updatedArtist.getGenre(), "Genre should match"));
    }

        @Test
        @DisplayName("Should save an artist")
        void save_ShouldSaveAnArtist() {
            ArtistPostRequestBody artistToBeSaved = ArtistCreator.createArtistPostRequestBody();

            ResponseEntity<ArtistDTO> response = testRestTemplate.exchange("/artists/create", HttpMethod.POST, new HttpEntity<>(artistToBeSaved), ArtistDTO.class);

            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertNotNull(response.getBody().getId());
            Assertions.assertNotNull(artistToBeSaved.getName(), response.getBody().getName());
        }

        @Test
        @DisplayName("Should not save an Artist if name is blank ")
        void shouldNotSaveArtistIfNameIsBlank() {
            ArtistPostRequestBody artistPostRequestBody = ArtistPostRequestBody.builder()
                    .name(" ")
                    .genre("Japenese shoegaze")
                    .build();

            ResponseEntity<ArtistDTO> response = testRestTemplate.exchange("/artists/create", HttpMethod.POST, new HttpEntity<>(artistPostRequestBody), ArtistDTO.class);

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("Should delete an artist")
        void delete_ShouldDeleteAnArtist() {
            Artist savedArtist = artistRepository.save(ArtistCreator.createArtistToBeSaved());

            Long idExpected = savedArtist.getId();

            ResponseEntity<Void> response = testRestTemplate.exchange("/artists/delete/{id}", HttpMethod.DELETE, new HttpEntity<>(savedArtist), Void.class, idExpected);

            Assertions.assertTrue(artistRepository.findById(savedArtist.getId()).isEmpty());

        }

        @Test
        @DisplayName("Should not delete an artist if not found by id")
        void delete_ShouldNotDeleteAnArtistIfNotFoundById() {

            ResponseEntity<Void> response = testRestTemplate.exchange("/artists/delete/{id}", HttpMethod.DELETE, null, Void.class, 999L);

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("Should not delete an artist if they have an album :)")
        void delete_shouldNotDeleteAnArtistIfTheyHaveAnAlbum() {
            Artist savedArtist = artistRepository.save(ArtistCreator.createArtistToBeSaved());
            Album albumToBeSavedToArtist = albumRepository.save(AlbumCreator.createAlbumToBeSaved(savedArtist));

            ResponseEntity<Void> response = testRestTemplate.exchange("/artists/delete/{id}", HttpMethod.DELETE, null, Void.class, savedArtist.getId());

            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
}

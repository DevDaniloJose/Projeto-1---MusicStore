package com.musicStore.api_loja_discos.integration;

import com.musicStore.api_loja_discos.Enum.Role;
import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.repository.AlbumRepository;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.*;
import com.musicStore.api_loja_discos.service.UserService;
import com.musicStore.api_loja_discos.util.AlbumCreator;
import com.musicStore.api_loja_discos.util.ArtistCreator;
import com.musicStore.api_loja_discos.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate testRestTemplate;



    private static final String USERNAME = "username";
    private static final String PASSWORD = "password123";
    private static final String ADMIN_USERNAME = "dan";
    @Autowired
    private AlbumRepository albumRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userRepository.save(User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role(Role.USER)
                .build());

        userRepository.save(User.builder()
                .username(ADMIN_USERNAME)
                .password(PASSWORD)
                        .role(Role.ADMIN)
                .build());
    }



    @Test
    @DisplayName("Signing up with valid credentials should return 201")
    void signingUpWithValidCredentials_returns201() {
        SignUpRequest request = new SignUpRequest("wyatt", PASSWORD);

        ResponseEntity<SignUpResponse> response = testRestTemplate.postForEntity("/users/signup", request, SignUpResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Signing up with invalid username should return 400")
    void signingUpWithInvalidUsername_shouldReturn400() {
        SignUpRequest request = new SignUpRequest("", PASSWORD);

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/users/signup", request, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Signing up with invalid password should return 400")
    void signingUpWithInvalidPassword_ShouldReturn400() {
        SignUpRequest request = new SignUpRequest("Kim gordon", "");

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/users/signup", request, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Signing up as an artist")
    void signingUpAsAnArtist() {
        ArtistSignUpRequest artist = ArtistCreator.createArtistPostRequestBody();

        ResponseEntity<ArtistDTO> response = testRestTemplate.postForEntity("/users/signup/artist", artist, ArtistDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Should permit promote user to admin IF the promoter is an admin already")
    void shouldPermitPromoteUserToAdmin_IFuserIsAnAdminAlready() {

        long regularUserId = userRepository.findByUsername(USERNAME).get().getId();

        String tokenAdmin = loginAndGetToken(ADMIN_USERNAME);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<SignUpResponse> response  = testRestTemplate.exchange("/users/promote/{id}", HttpMethod.POST, requestEntity, SignUpResponse.class, regularUserId);

        org.assertj.core.api.Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        org.assertj.core.api.Assertions.assertThat(response.getBody()).isNotNull();
    }

    private String loginAndGetToken(String username) {
        AuthRequest request = new AuthRequest(username, PASSWORD);

        AuthResponse response = testRestTemplate.postForEntity("/auth/login", request, AuthResponse.class).getBody();

        assertThat(response).isNotNull();
        return response.token();
    }

    @Test
    @DisplayName("Should not permit promote user to admin IF the promoter is not admin")
    void ShouldNotPermitPromoteUserToAdmin_IfPromoterIsNotAdmin() {
        AuthRequest request = new AuthRequest(USERNAME, PASSWORD);

        long targetUserId = userRepository.findByUsername(ADMIN_USERNAME).get().getId();

        String token = loginAndGetToken(USERNAME);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = testRestTemplate.exchange("/users/promote/{id}", HttpMethod.POST, requestEntity, Void.class, targetUserId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("should return a list of favorite albums from the user")
    void shouldReturnListOfFavoriteAlbums_FromUser() {

        ArtistDTO artist = ArtistCreator.createArtistDTO();
        AlbumDTO album = AlbumCreator.createAlbumDTO(artist);



        Long userId = 1L;
        ResponseEntity<AlbumDTO[]> response = testRestTemplate.getForEntity("/users/" + userId + "/showFavoriteAlbums", AlbumDTO[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();

        List<AlbumDTO> returnedList = List.of(response.getBody());

        assertThat(returnedList).isNotEmpty();
        assertThat(returnedList.getFirst().getTitle()).isEqualTo(album.getTitle());
    }

    @Test
    @DisplayName("should show userInfo")
    void shouldShowUserInfo() {
        User userEntity = UserCreator.createUserEntity();
        userRepository.save(userEntity);
        ResponseEntity<UserDTO> response = testRestTemplate.getForEntity("/users" + userEntity.getId() + "/showUserInfo", UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }


}

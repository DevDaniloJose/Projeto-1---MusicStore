package com.musicStore.api_loja_discos.integration;

import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.AuthRequest;
import com.musicStore.api_loja_discos.requests.AuthResponse;
import com.musicStore.api_loja_discos.requests.SignUpRequest;
import com.musicStore.api_loja_discos.requests.SignUpResponse;
import com.musicStore.api_loja_discos.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

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

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userRepository.save(User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role("USER")
                .build());

        userRepository.save(User.builder()
                .username(ADMIN_USERNAME)
                .password(PASSWORD)
                        .role("ADMIN")
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


}

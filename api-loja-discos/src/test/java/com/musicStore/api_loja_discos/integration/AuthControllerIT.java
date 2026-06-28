package com.musicStore.api_loja_discos.integration;

import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.repository.RefreshTokenRepository;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.requests.AuthRequest;
import com.musicStore.api_loja_discos.requests.AuthResponse;
import com.musicStore.api_loja_discos.util.ArtistCreator;
import io.jsonwebtoken.lang.Assert;
import org.assertj.core.api.Assertions;
import org.hibernate.mapping.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers // o java vai baixar e subir um banco de dados MYSQL 8.0 real isolado dentro de um container Docker
// container Docker temporário apenas pra essa classe de testes



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // isso sobe o contexto completo da aplicação
// spring boot (incluindo segurança, rotas e filtros)
// e abre uma porta aleatória real no meu computador pra simular o servidor rodando
public class AuthControllerIT {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0");

    @Autowired
   private UserRepository userRepository; // Vai ser usado pra limpar o db
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository; // usado pra limpar o db


    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password123";
    private static final String ADMIN_USERNAME = "adminuser";

    @BeforeEach // garante que esse método seja executado antes de cada um dos testes da classe. Se eu tenho 8 testes, o setUp()
    // vai rodar 8 vezes. O objetivo dele eh garantir que cada teste comece exatamente com o mesmo cenário controlado
    void setUp() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();


        userRepository.save(User.builder()
                .username(USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .role("USER")
                .build());

        userRepository.save(User.builder()
                .username(ADMIN_USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .role("ADMIN")
                .build());
    }

    //login ================

    @Test
    @DisplayName("Login with valid credentials should return 200 with token and refreshToken")

    void login_WithValidCredentials_Returns200WithTokens() {
        AuthRequest request = new AuthRequest(USERNAME, PASSWORD);

        ResponseEntity<AuthResponse> response = testRestTemplate.postForEntity("/auth/login", request, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotBlank();
        assertThat(response.getBody().refreshToken()).isNotNull();
    }

    @Test
    @DisplayName("Login with wrong password returns fucking 401")
    void login_WithWrongPassword_Returns401() {
        AuthRequest request = new AuthRequest(USERNAME, "wrongpassword");

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/auth/login", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    @DisplayName("Login with wrong username returns fucking 401")
    void login_WithWrongUsername_Returns401() {
        AuthRequest request = new AuthRequest("wrongUsername", PASSWORD);

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/auth/login", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ROTAS PROTEGIDAS ===============

    @Test
    @DisplayName("Protected Routes without token returns 401")
    void protectedRoutes_Returns401() {
        Artist saved = artistRepository.save(ArtistCreator.createArtistToBeSaved());

        ResponseEntity<Void> response = testRestTemplate.exchange("/artists/{id}", HttpMethod.GET, null, Void.class, saved.getId());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Protected routes with valid token returns 200")

    void protectedRoutesWithValidToken_Returns200() {
        Artist saved = artistRepository.save(ArtistCreator.createArtistToBeSaved());
        String token = loginAndGetToken(USERNAME);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Void> response = testRestTemplate.exchange("artists/{id}", HttpMethod.GET, new HttpEntity<>(headers), Void.class, saved.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("admin route with USER token returns 403")
    void adminRouteWithUserToken_returns403() {
        String token = loginAndGetToken(USERNAME);

        HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

        ResponseEntity<Void> response = testRestTemplate.exchange("artists/{id}", HttpMethod.GET, new HttpEntity<>(headers), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("admin route with ADMIN token returns 200")
    void adminRouteWithAdminToken_Returns200() {
        Artist saved = artistRepository.save(ArtistCreator.createArtistToBeSaved());
        String token = loginAndGetToken(ADMIN_USERNAME);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<ArtistDTO> response = testRestTemplate.exchange("/artists/{id}", HttpMethod.GET, new HttpEntity<>(headers), ArtistDTO.class, saved.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

// Refresh token


    @Test
    @DisplayName("Refresh with valid token returns 200 with new JWT")
    void refeshWithValidToken_Returns200NewJwt() {
        AuthRequest loginRequest = new AuthRequest(USERNAME, PASSWORD);

        AuthResponse loginResponse = testRestTemplate.postForEntity("/auth/login", loginRequest, AuthResponse.class).getBody();

        assertThat(loginResponse).isNotNull();

        String refreshToken = loginResponse.refreshToken().getToken();

        ResponseEntity<Map> response = testRestTemplate.postForEntity(
                "/auth/refresh",
                java.util.Map.of("refreshToken", refreshToken),
                Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    // HELPER

    private String loginAndGetToken(String username) {
        AuthRequest request = new AuthRequest(username, PASSWORD);

        AuthResponse response = testRestTemplate.postForEntity("/auth/login", request, AuthResponse.class).getBody();

        assertThat(response).isNotNull();
        return response.token();
    }

}

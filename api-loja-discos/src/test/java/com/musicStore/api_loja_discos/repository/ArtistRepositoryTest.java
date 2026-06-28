package com.musicStore.api_loja_discos.repository;

import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.service.ArtistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import  org.assertj.core.api.Assertions;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for artist Repository")
@Testcontainers

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class ArtistRepositoryTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0");

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void save_PersistArtist_WhenSuccessful() {
        Artist artistToBeSaved = createArtist();
        Artist artistSaved = this.artistRepository.save(artistToBeSaved);
        Assertions.assertThat(artistSaved).isNotNull();
        Assertions.assertThat(artistSaved.getId()).isNotNull();
        Assertions.assertThat(artistSaved.getName()).isEqualTo(artistToBeSaved.getName());
    }


    private Artist createArtist() {
       return Artist.builder()
                .genre("post-punk")
                .name("Siouxsie and the Banshees").build();
    }

}
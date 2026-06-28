package com.musicStore.api_loja_discos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.stereotype.Service;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class MusicApiApplicationTests {

    @Container
    @ServiceConnection
    static MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0");

	@Test
	void contextLoads() {
	}

}

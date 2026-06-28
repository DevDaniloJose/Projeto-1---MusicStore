package client;

import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.service.ArtistService;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Log4j2
public class SpringClient {

    static void main() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity("http://localhost:8080/artists/{id}", ArtistDTO.class);


    }

}

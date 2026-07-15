package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.Enum.Role;
import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.exceptions.*;
import com.musicStore.api_loja_discos.repository.AlbumRepository;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.requests.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.musicStore.api_loja_discos.mapper.ArtistMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final AlbumRepository albumRepository;
    private final UserService userService;

    public Page<ArtistDTO> listArtists(Pageable pageable) {
        Page<Artist> artistPage = artistRepository.findAll(pageable);
        return artistPage.map(artistMapper::toArtistDTO);

    }

    public ArtistDTO findArtistById(Long id) {
        //.map(artistMapper::toArtistDTO).orElseThrow(() -> new BadRequestException("Artist not found"));
      return artistRepository.findById(id).map(artistMapper::toArtistDTO).orElseThrow(() -> new BadRequestException("Artist not found"));

    }


    public ArtistDTO save(ArtistSignUpRequest artistPR) {
        Artist artist = artistMapper.toArtist(artistPR);

        return artistMapper.toArtistDTO(artistRepository.save(artist));

    }

    public void delete(long id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new BadRequestException("Artist not found"));

        if (!artist.getAlbums().isEmpty()) {
            throw new BusinessRuleException("It's not possible to delete an artist who already has registered albums. Delete their albums first.");
        }
        artistRepository.deleteById(id);

    }

    @Transactional

    public ArtistDTO signUpArtist(ArtistSignUpRequest request) {
        User savedUser = userService.createSystemUser(
                request.username(),
                request.password(),
                Role.ARTIST
        );


        Artist artist = artistMapper.toArtist(request);

        artist.setUser(savedUser);

        Artist savedArtist = artistRepository.save(artist);

        return artistMapper.toArtistDTO(savedArtist);


    }

    public List<Artist> filterByGenre(String genre) {
        return artistRepository.findByGenreIgnoreCase(genre);
    }

    public List<ArtistDTO> findByStageName(String stageName) {

        List<Artist> artists = artistRepository.findByStageNameContainingIgnoreCase(stageName).orElseThrow(() -> new ResourceNotFoundException("no Artist found in db with that name"));

        return artistMapper.toArtistDTOList(artists);


    }

// public List<ArtistDTO> findByFilters(Integer duration, Integer year) {
//     return artistRepository.findByFilters(year, duration);
// }

    public ArtistDTO replace(ArtistPutRequestBody artistPutRequestBody) {
        Artist artistDb = artistRepository.findById(artistPutRequestBody.getId()).orElseThrow(() -> new BadRequestException("Artist not found"));

        if (artistPutRequestBody.getGenre() != null) {
            artistDb.setGenre(artistPutRequestBody.getGenre());
        }

        if (artistPutRequestBody.getStageName() != null) {
            artistDb.setStageName(artistPutRequestBody.getStageName());
        }

        if (artistPutRequestBody.getUsername() != null) {
            artistDb.setUsername(artistPutRequestBody.getUsername());

        }

        Artist updated = artistRepository.save(artistDb);
        return artistMapper.toArtistDTO(updated);
    }


    public ArtistDTO searchOnMySystemUsingGetForEntity(Long id) {
        RestTemplate restTemplate = new RestTemplate();
      return  restTemplate.getForEntity("http://localhost:8080/artists/{id}", ArtistDTO.class, id).getBody();
    }

   public List<Map<String, Object>> exchangePracticing() {
       RestTemplate restTemplate = new RestTemplate();

       ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange("https://jsonplaceholder.typicode.com/users", HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {
       });

       return Optional.ofNullable(response.getBody()).orElse(Collections.emptyList());
   }
   
   public ArtistDTO postExchange(ArtistDTO artistDto) {
       RestTemplate restTemplate = new RestTemplate();

       String url = "http://localhost:8080/artists";

       HttpEntity<ArtistDTO> artistDTOHttpEntity = new HttpEntity<>(artistDto, createJsonHeader());

       ResponseEntity<ArtistDTO> response = restTemplate.exchange(url, HttpMethod.POST, artistDTOHttpEntity, ArtistDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new ResourceNotFoundException("Artist not found or service is unavailable"));
        } else {
            throw new ServiceInvocationException("Error on external api: " + response.getStatusCode());
        }
   }

   private static HttpHeaders createJsonHeader() {
       HttpHeaders httpHeaders = new HttpHeaders();
       httpHeaders.setContentType(MediaType.APPLICATION_JSON);
       return httpHeaders;
   }

   public void putExchange(ArtistDTO artistDto) {
       RestTemplate restTemplate = new RestTemplate();

       String url = "http://localhost:8080/artists/" + artistDto.getId();
       HttpEntity<ArtistDTO> artistDTOHttpEntity = new HttpEntity<>(artistDto, createJsonHeader());

       restTemplate.exchange(url, HttpMethod.PUT, artistDTOHttpEntity, Void.class);
   }

   public void deleteExchange(ArtistDTO artistDto) {
       RestTemplate restTemplate = new RestTemplate();

       String url = "http://localhost:8080/artists/" + artistDto.getId();
       HttpEntity<ArtistDTO> artistDTOHttpEntity = new HttpEntity<>(artistDto, createJsonHeader());
       restTemplate.exchange(url, HttpMethod.DELETE, artistDTOHttpEntity, Void.class);
   }

   public List<Album> showAlbunsOfArtist(Long id) {
       Artist artist = artistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("The artist does not exist in DataBase"));
       return artist.getAlbums();
   }


}


package com.musicStore.api_loja_discos.controller;


import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.*;
import com.musicStore.api_loja_discos.service.ArtistService;
import com.musicStore.api_loja_discos.mapper.AlbumMapper;
import com.musicStore.api_loja_discos.repository.AlbumRepository;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor

public class ArtistController {

    private final ArtistService artistService;
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final ArtistRepository artistRepository;
    @GetMapping("/list-artists")
    public ResponseEntity<Page<ArtistDTO>> listArtists(Pageable pageable) {

        return ResponseEntity.ok(artistService.listArtists(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> findByIdOrElseThrow(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.findArtistById(id));
    }

  //  @GetMapping("/filter")
  //  public ResponseEntity<List<ArtistDTO>> filter(
  //          @RequestParam(required = false) Integer year,
  //          @RequestParam(required = false) Integer duration) {
//
  //      List<ArtistDTO> filtered = artistService.findByFilters(duration, year );
//
  //      return ResponseEntity.ok(filtered);
  //  }

    @PostMapping("/create")
    public ResponseEntity<ArtistDTO> addArtist(@Valid @RequestBody ArtistSignUpRequest artist) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.save(artist));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
      artistService.delete(id);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/replace/{id}")
    public ResponseEntity<ArtistDTO> replace(@RequestBody ArtistPutRequestBody artistPutRequestBody) {
     return ResponseEntity.ok(artistService.replace(artistPutRequestBody));
    }

    @GetMapping(path = "/find")
    public List<ArtistDTO> findByStageName(@RequestParam(required = false) String stageName) {
     return artistService.findByStageName(stageName);
    }

    @GetMapping(path = "/test-entity/{id}")
    public ResponseEntity<ArtistDTO> searchOnMySystemUsingGetForEntity(@PathVariable Long id) {
      return ResponseEntity.ok(artistService.searchOnMySystemUsingGetForEntity(id));
    }

  @GetMapping(path = "/exchange")
    public ResponseEntity<List<Map<String,Object>>> exchangePractice() {
       return ResponseEntity.ok(artistService.exchangePracticing());
  }

  @GetMapping(path = "/{id}/albums")
  public ResponseEntity<List<Album>> seeAlbumsOfArtist(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.showAlbunsOfArtist(id));
  }










}

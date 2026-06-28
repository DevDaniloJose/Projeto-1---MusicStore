package com.musicStore.api_loja_discos.controller;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.requests.AlbumDTO;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import com.musicStore.api_loja_discos.service.AlbumService;
import com.musicStore.api_loja_discos.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final ArtistService artistService;
    private final AlbumService albumService;


    @PostMapping("/create")
    public ResponseEntity<AlbumDTO> save(@RequestBody @Valid AlbumDTO albumDTO) {
        return new ResponseEntity<>(albumService.save(albumDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AlbumDTO> delete(@PathVariable @Valid Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

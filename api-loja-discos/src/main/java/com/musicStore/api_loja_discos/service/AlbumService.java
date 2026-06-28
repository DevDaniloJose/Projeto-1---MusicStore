package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.exceptions.BadRequestException;
import com.musicStore.api_loja_discos.exceptions.InvalidReleaseYearException;
import com.musicStore.api_loja_discos.mapper.ArtistMapper;
import com.musicStore.api_loja_discos.repository.AlbumRepository;
import com.musicStore.api_loja_discos.requests.AlbumDTO;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import lombok.RequiredArgsConstructor;
import com.musicStore.api_loja_discos.mapper.AlbumMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

        private final AlbumMapper albumMapper;
        private final ArtistService artistService;
        private final ArtistMapper artistMapper;

   private final AlbumRepository albumRepository;

    public List<AlbumDTO> listAlbums() {
       return albumRepository.findAll().stream().map(albumMapper::toAlbumDTO).toList();
    }

  public AlbumDTO findByIdOrElseThrowException(Long id) {
      Album album = albumRepository.findById(id).orElseThrow(() -> new BadRequestException("Album not found"));
     return albumMapper.toAlbumDTO(album);
  }

    @Transactional(rollbackFor = Exception.class)
  public AlbumDTO save(AlbumDTO albumDTO) {
        artistService.findArtistById(albumDTO.getArtist().getId());
        if (albumDTO.getReleaseYear() > Year.now().getValue()) {
            throw new InvalidReleaseYearException("Album must be from current  or less.");
        }
        Album album = albumRepository.save(albumMapper.toAlbum(albumDTO));
      return albumMapper.toAlbumDTO(album);
  }

  public AlbumDTO replace(AlbumDTO albumDTO) {
      ArtistDTO artist = artistService.findArtistById(albumDTO.getArtist().getId());
      Album albumDb = albumRepository.findById(albumDTO.getId()).orElseThrow(() -> new BadRequestException("Album not found"));

      albumDb.setTitle(albumDTO.getTitle());
      albumDb.setReleaseYear(albumDTO.getReleaseYear());
      albumDb.setDurationMinutes(albumDTO.getDurationMinutes());

      Artist newArtist = artistMapper.toArtist(artist);
      albumDb.setArtist(newArtist);


      AlbumDTO album = albumMapper.toAlbumDTO(albumDb);
      Album albumDto = albumRepository.save(albumDb);
      return albumMapper.toAlbumDTO(albumDto);
  }

  public void delete(Long id) {
     findByIdOrElseThrowException(id);
      albumRepository.deleteById(id);
  }

  public List<AlbumDTO> findByYear(Integer year) {
      List<Album> albums = albumRepository.findByReleaseYear(year);
      if (albums.isEmpty()) {
          throw new BadRequestException("no albums found by the year of" + year);
      }

      return  albums.stream().map(albumMapper::toAlbumDTO).toList();
  }

//@Transactional(rollbackFor = Exception.class)
//public AlbumDTO savewWithBug(AlbumDTO albumDTO) {
//    Album album = albumMapper.toAlbum(albumDTO);
//    albumRepository.save(album);
//    throw new RuntimeException("Erro de teste");
//}





}

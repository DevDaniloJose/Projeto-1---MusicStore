package com.musicStore.api_loja_discos.repository;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.requests.AlbumDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByReleaseYear(Integer year);

    List<Album> findByArtistId(Long artistId);

}

package com.musicStore.api_loja_discos.repository;

import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.requests.ArtistDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByGenreIgnoreCase(String genre);

// @Query("SELECT DISTINCT a FROM Artist a LEFT JOIN a.albums al " +
//         "WHERE (:year IS NULL OR al.releaseYear = :year) " +
//         "AND (:duration IS NULL OR al.durationMinutes = :duration)")
// List<ArtistDTO> findByFilters(@Param("year") Integer year, @Param("duration") Integer duration);

    Optional<List<Artist>> findByStageNameContainingIgnoreCase(String stageName);
}

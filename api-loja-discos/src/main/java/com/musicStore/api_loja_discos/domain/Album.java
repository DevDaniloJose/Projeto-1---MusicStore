package com.musicStore.api_loja_discos.domain;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "albums")
@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer releaseYear;
    private Integer durationMinutes;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
    private String title;

}

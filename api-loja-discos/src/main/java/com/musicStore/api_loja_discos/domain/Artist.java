package com.musicStore.api_loja_discos.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artists")
public class Artist {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private  Long id;
   @Column(nullable = false)
    private  String username;
    @Column(nullable = false)
    private  String genre;

    @Column(nullable = false)
    private String stageName;
    @Column(nullable = false)
    private String professionalEmail;
    private String bio;
    @Column(nullable = false)
    private String country;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


   //mappedBy - indicates the attributes's name on album class
    // CascadeType.ALL: if artist is saved, album is saved too
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Album> albums = new ArrayList<>();
}

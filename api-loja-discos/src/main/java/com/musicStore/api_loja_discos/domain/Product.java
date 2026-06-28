package com.musicStore.api_loja_discos.domain;


import com.musicStore.api_loja_discos.Enum.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;
    private ProductType type;

        @ManyToOne
        @JoinColumn(name = "artist_id")
        private Artist artist;


}

package com.musicStore.api_loja_discos.repository;

import com.musicStore.api_loja_discos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByType(Product product);

    List<Product> findByArtistId(Long artistId);

    List<Product> findByNameContianingIgnoreCase(String name);

    List<Product> findByStockGreatherThan(int value);

}

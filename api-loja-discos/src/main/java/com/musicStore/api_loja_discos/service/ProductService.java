package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.domain.Product;
import com.musicStore.api_loja_discos.mapper.ProductMapper;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.repository.ProductRepository;
import com.musicStore.api_loja_discos.requests.CreateProductRequest;
import com.musicStore.api_loja_discos.requests.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ArtistRepository artistRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductResponse saveProduct(CreateProductRequest request) {
        Artist artist = artistRepository.findById(request.artistId()).orElseThrow(() -> new EntityNotFoundException("Artist not found in DB"));

        Product product = productMapper.toProduct(request);

        product.setArtist(artist);

        productRepository.save(product);

       return productMapper.toProductResponse(product);
    }


    public List<Product> findAvailableProducts() {
        return productRepository.findByStockGreaterThan(0);
    }
}

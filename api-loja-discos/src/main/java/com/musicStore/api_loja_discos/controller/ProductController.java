package com.musicStore.api_loja_discos.controller;

import com.musicStore.api_loja_discos.requests.CreateProductRequest;
import com.musicStore.api_loja_discos.requests.ProductResponse;
import com.musicStore.api_loja_discos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/createProduct")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.saveProduct(request));
    }

}

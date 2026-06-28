package com.musicStore.api_loja_discos.mapper;

import com.musicStore.api_loja_discos.domain.Product;
import com.musicStore.api_loja_discos.requests.CreateProductRequest;
import com.musicStore.api_loja_discos.requests.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

      Product toProduct(CreateProductRequest request);

      ProductResponse toProductResponse(Product product);
}

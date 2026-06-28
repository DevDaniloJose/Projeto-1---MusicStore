package com.musicStore.api_loja_discos.requests;

import com.musicStore.api_loja_discos.Enum.ProductType;

import java.math.BigDecimal;

public record ProductResponse(
                              Long id,
                              String name,
                              BigDecimal price,
                              int stock,
                              ProductType productType,
                              Long artistId) {
}




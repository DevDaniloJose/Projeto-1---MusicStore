package com.musicStore.api_loja_discos.requests;

import com.musicStore.api_loja_discos.Enum.Role;
import lombok.Builder;

import java.util.List;

@Builder
public record UserDTO(
        Long id,
        String username,
        Role role,
        List<AlbumDTO> favoriteAlbums)
{}

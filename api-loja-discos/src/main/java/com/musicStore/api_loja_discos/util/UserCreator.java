package com.musicStore.api_loja_discos.util;

import com.musicStore.api_loja_discos.Enum.Role;
import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.requests.AlbumDTO;
import com.musicStore.api_loja_discos.requests.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserCreator {

    public static UserDTO createUserDTO() {
        List<AlbumDTO> favoriteAlbums = new ArrayList<>();
       return UserDTO.builder()
                .id(1L)
                .username("Masayoshi_Takanaka")
                .role(Role.USER)
                .favoriteAlbums(favoriteAlbums)
                .build();
    }

    public static User createUserEntity() {
        return User.builder()
                .username("Masayoshi_Takanaka")
                .role(Role.USER)
                .favoriteAlbums(new ArrayList<>())
                .build();
    }
    
    public static User createAdminUserEntity() {
        return User.builder().id(1L)
                .username("Masayoshi_takanaka")
                .password("password")
                .role(Role.ADMIN)
                .favoriteAlbums(new ArrayList<>())
                .build();
    }



}

package com.musicStore.api_loja_discos.mapper;

import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.requests.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public abstract class UserMapper {

    public static UserDTO toUserDTO(User user) {
        return null;
    }

}

package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.Enum.Role;
import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.exceptions.BadRequestException;
import com.musicStore.api_loja_discos.mapper.AlbumMapper;
import com.musicStore.api_loja_discos.mapper.UserMapper;
import com.musicStore.api_loja_discos.repository.AlbumRepository;
import com.musicStore.api_loja_discos.repository.ArtistRepository;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.*;
import com.musicStore.api_loja_discos.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AlbumRepository albumRepository;
  private final ArtistRepository artistRepository;
  private final AlbumMapper albumMapper;
  private final JWTService jwtService;


    @Override
    @Cacheable(value = "userInfo", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User Not Found in Db"));
        return new UserPrincipal(user);
    }

    public SignUpResponse saveUser(SignUpRequest userDTO) {

        String encodedPassword = passwordEncoder.encode(userDTO.password());

        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        com.musicStore.api_loja_discos.domain.User newUser = com.musicStore.api_loja_discos.domain.User.builder()
                .username(userDTO.username())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        com.musicStore.api_loja_discos.domain.User saved = userRepository.save(newUser);
        String token = jwtService.generateToken(newUser.getUsername());

        return new SignUpResponse(saved.getUsername(), saved.getId(), token);
    }



    @Transactional
    public SignUpResponse promoteToAdmin(String promoterUsername, Long targetUser) {
        com.musicStore.api_loja_discos.domain.User promoter = userRepository.findByUsername(promoterUsername)
                .orElseThrow(() -> new BadRequestException("Promoter not found"));


        isAdmin(promoter.getId());

        com.musicStore.api_loja_discos.domain.User target = userRepository.findById(targetUser).orElseThrow(() -> new BadRequestException("Target user not found"));

        target.setRole(Role.ADMIN);
        com.musicStore.api_loja_discos.domain.User save = userRepository.save(target);



        return new SignUpResponse(save.getUsername(), save.getId(), null);
    }

    public UserDTO findByUsername(String username) {
         return userRepository.findByUsername(username).map(UserMapper::toUserDTO).orElseThrow(() -> new BadRequestException("Promoter not found"));
    }



    public boolean isAdmin(long id) {
        com.musicStore.api_loja_discos.domain.User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("User is not admin");
        }
        return true;

    }

    @Transactional
    public List<Album> toFavoriteAlbum(String username, Long albumId) {
        com.musicStore.api_loja_discos.domain.User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not foud in db"));
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new EntityNotFoundException("Album not found in db"));

        List<Album> favoriteAlbums = user.getFavoriteAlbums();
      favoriteAlbums.add(album);
      userRepository.save(user);
      return favoriteAlbums;
    }

    public List<AlbumDTO> listFavoriteAlbumList(String username) {
        com.musicStore.api_loja_discos.domain.User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found in db"));
        return user.getFavoriteAlbums().stream().map(albumMapper::toAlbumDTO).collect(Collectors.toList());
    }


    public UserDTO showUserInfo(String username) {

        com.musicStore.api_loja_discos.domain.User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User is not in db"));

        List<AlbumDTO> favoriteAlbums = user.getFavoriteAlbums().stream().map(albumMapper::toAlbumDTO).collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getUsername(), user.getRole(), favoriteAlbums);
    }

    public User createSystemUser(String username, String rawPassword, Role role) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return userRepository.save(user);
    }
}

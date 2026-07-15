package com.musicStore.api_loja_discos.controller;

import com.musicStore.api_loja_discos.domain.Album;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.*;
import com.musicStore.api_loja_discos.service.ArtistService;
import com.musicStore.api_loja_discos.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ArtistService artistService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> register(@RequestBody @Valid SignUpRequest user) {
        SignUpResponse response = userService.saveUser(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("signup/artist")
    public ResponseEntity<ArtistDTO> registerArtist(@RequestBody @Valid ArtistSignUpRequest user) {
        ArtistDTO response = artistService.signUpArtist(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/promote/{id}")
    public ResponseEntity<SignUpResponse> promoteToAdmin(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long targetUserId) {
    SignUpResponse updatedUser = userService.promoteToAdmin(userDetails.getUsername(), targetUserId);
     return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/favorites/{albumId}")
    public ResponseEntity<List<Album>> favoriteAlbum(@AuthenticationPrincipal UserDetails loggedInUser, @PathVariable Long albumId) {
        return ResponseEntity.ok(userService.toFavoriteAlbum(loggedInUser.getUsername(), albumId));
    }

    @GetMapping("/showFavoriteAlbums")
    public ResponseEntity<List<AlbumDTO>> showFavoriteAlbumsList(@AuthenticationPrincipal UserDetails loggedInUser) {
        return ResponseEntity.ok(userService.listFavoriteAlbumList(loggedInUser.getUsername()));
    }

    @GetMapping("/showUserInfo/")
    public ResponseEntity<UserDTO> showUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.showUserInfo(userDetails.getUsername()));
    }

    @GetMapping("/showMyInfoRoute/{userId}")
    public ResponseEntity<UserDetails> showUserInfoRoute(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.loadUserByUsername(userDetails.getUsername()));
    }

}

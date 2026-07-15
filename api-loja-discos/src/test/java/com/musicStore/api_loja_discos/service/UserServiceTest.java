package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.Enum.Role;
import com.musicStore.api_loja_discos.domain.Artist;
import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.exceptions.BadRequestException;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.AuthRequest;
import com.musicStore.api_loja_discos.requests.SignUpRequest;
import com.musicStore.api_loja_discos.requests.SignUpResponse;
import com.musicStore.api_loja_discos.requests.UserDTO;
import com.musicStore.api_loja_discos.util.ArtistCreator;
import com.musicStore.api_loja_discos.util.UserCreator;
import com.musicStore.api_loja_discos.util.UserSignUpRequestCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;





    @Test
    @DisplayName("Should find an user by username and transform the user into userDetails")
      void shouldFindUserByUsernameAndMapIntoUserDetails() {


        User userBuilder = User.builder()
                .username("Sade")
                .password("senha123")
                .role(Role.valueOf("USER"))
                .build();

        Mockito.when(userRepository.findByUsername(userBuilder.getUsername())).thenReturn(Optional.of(userBuilder));

        UserDetails result = userService.loadUserByUsername(userBuilder.getUsername());


        Assertions.assertThat(result).isNotNull();
    }

 @Test
 @DisplayName("Should save User on DB after their sign up")
        void shouldSaveUserOnDbAfterSigningUp() {

     SignUpRequest request = new SignUpRequest("Sade", "password");



     Mockito.when(passwordEncoder.encode(request.password())).thenReturn(request.password());
     Mockito.when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

     User user = User.builder()
             .username(request.username())
             .id(1L)
             .password(request.password())
             .role(Role.valueOf("USER"))
             .build();

     Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

     SignUpResponse signUpResponse = userService.saveUser(request);

     Assertions.assertThat(signUpResponse).isNotNull();
     Assertions.assertThat(signUpResponse.username()).isEqualTo("Sade");
     Assertions.assertThat(signUpResponse.id()).isEqualTo(1L);
     }

     @Test
     @DisplayName("should throw bad requestException when trying to save user on db and username already exists")
     void shouldThrowBadRequestException_WhenUsernameAlreadyExistsInDb() {

         SignUpRequest signUpRequest = UserSignUpRequestCreator.createSignUpRequest();
         User userEntity = UserCreator.createUserEntity();
         Mockito.when(userRepository.findByUsername(signUpRequest.username())).thenReturn(Optional.of(userEntity));


         org.junit.jupiter.api.Assertions.assertThrows(BadRequestException.class, () -> userService.saveUser(signUpRequest));
     }

     @Test
    @DisplayName("Should promote user to admin")
    void shouldPromoteUserToAdmin() {

         User promoter = User.builder()
                 .username("Sade")
                 .password("password")
                 .id(2L)
                 .role(Role.valueOf("ADMIN"))
                 .build();

         User targetUser = User.builder()
                         .username("target")
                         .password("password")
                         .role(Role.valueOf("USER")).id(1L)
                          .build();


         Mockito.when(userRepository.findByUsername(promoter.getUsername())).thenReturn(Optional.of(promoter));
         Mockito.when(userRepository.findById(promoter.getId())).thenReturn(Optional.of(promoter));
         Mockito.when(userRepository.findById(targetUser.getId())).thenReturn(Optional.of(targetUser));

        Mockito.when(userRepository.save(any(User.class))).thenReturn(targetUser);

         SignUpResponse response = userService.promoteToAdmin(promoter.getUsername(), targetUser.getId());


         Assertions.assertThat(response).isNotNull();
         Assertions.assertThat(response.username()).isEqualTo("target");
         Assertions.assertThat(response.id()).isEqualTo(1L);
         Assertions.assertThat(targetUser.getRole()).isEqualTo("ADMIN");
     }

     @Test
    @DisplayName("should throw bad request exception if promoter not found by username")
    void shouldThrowBadRequestException_IfPromoterNotFoundByTheirUsername() {
         User adminUserEntity = UserCreator.createAdminUserEntity();
         User userEntity = UserCreator.createUserEntity();
         Mockito.when(userRepository.findByUsername(adminUserEntity.getUsername())).thenReturn(Optional.empty());

         org.junit.jupiter.api.Assertions.assertThrows(BadRequestException.class, () -> userService.promoteToAdmin(adminUserEntity.getUsername(), userEntity.getId()));

     }


     @Test
    @DisplayName("should throw bad request exception if promoter is not admin")
    void shouldThrowBadRequestExceptionIfPromoter_IsNotAdmin() {
         User userEntity = UserCreator.createUserEntity();
         Mockito.when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

         org.junit.jupiter.api.Assertions.assertThrows(BadRequestException.class, () -> userService.isAdmin(userEntity.getId()));
     }

 }

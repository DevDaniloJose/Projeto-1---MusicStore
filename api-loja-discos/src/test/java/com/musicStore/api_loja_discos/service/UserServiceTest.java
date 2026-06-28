package com.musicStore.api_loja_discos.service;

import com.musicStore.api_loja_discos.domain.User;
import com.musicStore.api_loja_discos.repository.UserRepository;
import com.musicStore.api_loja_discos.requests.AuthRequest;
import com.musicStore.api_loja_discos.requests.SignUpRequest;
import com.musicStore.api_loja_discos.requests.SignUpResponse;
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
                .role("USER")
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
             .role("USER")
             .build();

     Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

     SignUpResponse signUpResponse = userService.saveUser(request);

     Assertions.assertThat(signUpResponse).isNotNull();
     Assertions.assertThat(signUpResponse.username()).isEqualTo("Sade");
     Assertions.assertThat(signUpResponse.id()).isEqualTo(1L);
     }

     @Test
    @DisplayName("Should promote user to admin")
    void shouldPromoteUserToAdmin() {



         User promoter = User.builder()
                 .username("Sade")
                 .password("password")
                 .id(2L)
                 .role("ADMIN")
                 .build();

         User targetUser = User.builder()
                         .username("target")
                         .password("password")
                         .role("USER").id(1L)
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


 }

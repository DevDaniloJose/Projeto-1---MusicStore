package com.musicStore.api_loja_discos.security;

import com.musicStore.api_loja_discos.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.http.UserDetailsServiceFactoryBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestBody;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthTokenFilter authTokenFilter;



    @Bean
    public SecurityFilterChain basicAuthSecurityFilterChain(HttpSecurity http) throws Exception{
      return  http
              .csrf(AbstractHttpConfigurer::disable)
              .authorizeHttpRequests(auth ->{
                  auth.requestMatchers("/auth/**").permitAll();
                  auth.requestMatchers("/artists/list-artists").permitAll();
                  auth.requestMatchers("/artists/{id}/albums").permitAll();
                  auth.requestMatchers("/users/favorites/{albumId}").permitAll();
                  auth.requestMatchers("/users/showFavoriteAlbums/").permitAll();
                  auth.requestMatchers("/users/showUserInfo/").permitAll();
                  auth.requestMatchers("/users/showMyInfoRoute/{userId}").permitAll();
                  auth.requestMatchers("/auth/login").permitAll();
                  auth.requestMatchers("/users/signup/artist").permitAll();
                  auth.requestMatchers("/artists/{id}").hasRole("ADMIN");
                  auth.requestMatchers("/users/signup").permitAll();
                  auth.requestMatchers("/artists/delete/{id}/**").hasRole("ADMIN");
                  auth.requestMatchers("/artists/signup/**").permitAll();
                  auth.requestMatchers("/users/promote/**").hasRole("ADMIN");
                  auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //  Não cria sessão no servidor; cada requisição deve trazer o JWT novamente.
              .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
              .exceptionHandling(ex -> ex
                      .authenticationEntryPoint((request, response, authException) ->  {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

          response.setContentType("application/json");
          response.getWriter().write("""
                            {"error": "Authentication credentials were not provided or are invalid."}
                        """);
        })
                      .accessDeniedHandler((request, response, accessDeniedException) -> {
                          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                          response.setContentType("application/json");
                          response.getWriter().write("""
                            {"error": "You don't have the required permission."}
                        """);
                      })
              )
              .build();
    }

    /*
STATELESS = a API não guarda sessão do usuário no servidor.

Em aplicações tradicionais:
- O usuário faz login
- O servidor cria uma sessão
- Guarda informações (usuário, permissões, etc.)
- Nas próximas requisições usa essa sessão

Em APIs com JWT:
- O servidor NÃO guarda nada
- Cada requisição precisa trazer o token novamente
- O token já contém as informações necessárias

Ex:
Requisição 1 -> envia JWT -> valida -> responde
Requisição 2 -> envia JWT -> valida novamente -> responde

Sem isso, o Spring Security pode tentar criar sessões automaticamente,
o que não faz sentido em autenticação baseada em token.
*/

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
        return builder.build();
    }


}

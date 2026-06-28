package com.musicStore.api_loja_discos.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

 //

@Getter
@Setter
@NoArgsConstructor

// ======================================================================================
// 🧩 POR QUE O @NoArgsConstructor É OBRIGATÓRIO AQUI?
//
// O Hibernate (a ferramenta que o Spring usa para conversar com o MySQL) é uma "fábrica"
// automatizada. Quando você faz uma busca no banco (ex: findById), o Hibernate precisa
// instanciar (criar) o objeto na memória do Java para depois preencher os dados dele.
//
// Para fazer essa criação inicial às cegas, o Hibernate EXIGE a existência de um
// construtor vazio (sem nenhum parâmetro). Se você não colocar o @NoArgsConstructor,
// o Spring vai estourar um erro de "No default constructor for entity" na sua cara.
// ======================================================================================
@AllArgsConstructor

// ======================================================================================
// 🧱 POR QUE COLOCAMOS O @AllArgsConstructor JUNTO?
//
// No Java, se você não escreve nenhum construtor, a linguagem te dá um vazio de graça.
// Porém, a partir do momento em que o Lombok cria o @NoArgsConstructor, o Java perde
// a capacidade de gerar outros construtores automaticamente.
//
// Nós colocamos o @AllArgsConstructor para ter a LIBERDADE de, se quisermos no nosso
// código Java/Service, criar um objeto passando todos os dados de uma vez só nos
// parênteses (ex: new RefreshToken(1L, user, "token123", instant)), o que é excelente
// principalmente na hora de escrever TESTES UNITÁRIOS automatizados para a API.
// ======================================================================================


@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private String device;



}


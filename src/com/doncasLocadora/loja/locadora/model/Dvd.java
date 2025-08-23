package com.doncasLocadora.loja.locadora.model;

import Interface.Contrato;

public class Dvd implements Contrato {

    private String titulo;
    private String artista;
    private boolean isDisponivel;
    private int contador;

    public Dvd(String titulo, String artista,boolean isDisponivel) {
        this.titulo = titulo;
        this.artista = artista;
        this.isDisponivel = isDisponivel;
    }

    @Override
    public void alugar() {
        contador++;
        if (isDisponivel) {
            isDisponivel = false;
            System.out.println(titulo + " - " + artista + " alugado com sucesso!");
        }
    }

    @Override
    public void devolver() {
        if (!isDisponivel) {
            isDisponivel = true;
            System.out.println(titulo + " - " + artista + " devolvido com sucesso!");
        }
    }

    public String getArtista() {
        return artista;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponivel() {
        return isDisponivel;
    }

    public int getContador() {
        return contador;
    }

    @Override
    public String toString() {
        String status = isDisponivel ? "Disponível" : "Alugado";
        return "Dvd: " + titulo + " - " + artista + " [" + status + "]";

}
    }

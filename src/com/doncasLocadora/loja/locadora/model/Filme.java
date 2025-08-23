package com.doncasLocadora.loja.locadora.model;

import Interface.Contrato;

public class Filme implements Contrato {
   private String titulo;
    private String genero;
    private boolean isDisponivel;
    private int contador;


    public Filme(String titulo, String genero, boolean isDisponivel) {

        this.titulo = titulo;
        this.genero = genero;
        this.isDisponivel = isDisponivel;

    }

    public void alugar() {
        contador++;
        if (isDisponivel) {
            isDisponivel = false;
        }
    }

    public void devolver() {
        if (!isDisponivel) {
            isDisponivel = true;
        }
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
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
        return "Filme: " + titulo + " - " + genero + " [" + status + "] - Alugado" + contador + " vezes" ;
    }
}

package operacao;

import Interface.Contrato;
import com.doncasLocadora.loja.locadora.exception.ListaVazia;
import com.doncasLocadora.loja.locadora.exception.NomeNaoEncontrado;
import com.doncasLocadora.loja.locadora.model.Dvd;
import com.doncasLocadora.loja.locadora.model.Filme;

import java.util.ArrayList;
import java.util.Collections;

public class Locadora {

    private ArrayList<Filme> listaFilmes;
    private ArrayList<Dvd> listaDvds;
    private ArrayList<Contrato> listaTudo;

    public Locadora() {
        listaFilmes = new ArrayList<>();
        listaDvds = new ArrayList<>();
        listaTudo = new ArrayList<>();
    }


    public void adicionarFilme(String titulo, String genero, boolean isDisponivel) {
        Filme filme = new Filme(titulo, genero, isDisponivel);
        listaFilmes.add(filme);
        listaTudo.add(filme);
        System.out.println(titulo + " - adicionado");
    }

    public void adicionarDvd(String titulo, String artista, boolean isDisponivel) {
        Dvd dvd = new Dvd(titulo, artista, isDisponivel);
        listaDvds.add(dvd);
        listaTudo.add(dvd);
        System.out.println(titulo + " - adicionado!");
    }

    public void listarFilmes() throws ListaVazia {
        if (listaFilmes.isEmpty()) {
            throw new ListaVazia("Lista fazia fi");
        } else {
            for (Filme item : listaFilmes) {
                String status = item.isDisponivel() ? "Disponível" : " Alugado";
                System.out.println(item.getTitulo() + " - " + item.getGenero() + " [" + status + "]");
            }
        }
    }

    public void listarDvds() throws ListaVazia{
        if (listaDvds.isEmpty()) {
            throw new ListaVazia("Ta vazia..... fi");
        } else {
            for (Dvd item : listaDvds) {
                String status = item.isDisponivel() ? "Disponível" : " Alugado";
                System.out.println(item.getTitulo() + " - " + item.getArtista() + " [" + status + "]");
            }
        }
    }

    public void listarTudo() throws ListaVazia {
        if (listaTudo.isEmpty()) {
            throw new ListaVazia("Não há filmes ou dvds disponíveis");
        } else {
            for (Contrato item : listaTudo) {
                System.out.println(item);
            }
        }
    }

    public void listarMaisAlugados() throws ListaVazia {
        if (listaTudo.isEmpty()) {
            throw new ListaVazia("Lista vazia.");
        } else {
          listaTudo.sort((a,b) -> b.getContador() - a.getContador());
            for (Contrato item : listaTudo) {
                System.out.println("Mais alugado: " + item.getTitulo() + " [" + item.getContador() + " Alugueis]") ;
            }
        }
    }


    public void alugarFilmePorNome(String nome) throws NomeNaoEncontrado {
        boolean encontrado = false;
        for (Filme item : listaFilmes) {
            if (item.getTitulo().equalsIgnoreCase(nome)) {
                item.alugar();
                System.out.println(nome + " - Alugado com sucesso");
                encontrado = true;
                break;
            }
        } if (encontrado == false) {
            throw new NomeNaoEncontrado("Num deu pra encontra '-'");
        }
    }

    public void alugarDvdPorNome(String nome) throws NomeNaoEncontrado {
        boolean encontrado = false;
        for (Dvd item : listaDvds) {
            if (item.getTitulo().equalsIgnoreCase(nome)) {
                item.alugar();
                System.out.println(item.getTitulo() + " alugado com sucesso.");
                encontrado = true;
                break;
            }
        } if (encontrado == false) {
            throw new NomeNaoEncontrado("Num deu pra encontra '-'");
        }
    }

    public void devolverPorNome(String nome) throws NomeNaoEncontrado {
        boolean encontrado = false;
        for (Contrato item : listaTudo) {
           if (item.getTitulo().equalsIgnoreCase(nome)) {
               item.devolver();
               System.out.println(item.getTitulo() + "devolvido.");
               encontrado = true;
               break;
           }
        } if (encontrado == false) {
            throw new NomeNaoEncontrado("Num deu pra encontra '-'");
        }
    }

    public void buscarPorTitulo(String palavra) {
        for (Contrato item : listaTudo) {
            if (item.getTitulo().toLowerCase().contains(palavra.toLowerCase())) {
                System.out.println(item);
            }
        }
    }

}

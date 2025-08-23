package operacao;

import com.doncasLocadora.loja.locadora.exception.ListaVazia;
import com.doncasLocadora.loja.locadora.exception.NomeNaoEncontrado;

public class operacao {

    public static void main(String[] args) throws ListaVazia, NomeNaoEncontrado {
        Locadora locadora = new Locadora();

        locadora.adicionarFilme("O Hobbit", "Aventura", true);
        locadora.adicionarFilme("Senhor dos Aneís", "Aventura", true);
        locadora.adicionarDvd("Sick!!!", "Machine girl", true);

        locadora.listarFilmes();
        locadora.listarDvds();
        locadora.alugarFilmePorNome("O Hobbit");
        locadora.alugarDvdPorNome("Sick!!!");
        locadora.alugarDvdPorNome("Sick!!!");
        locadora.alugarDvdPorNome("Sick!!!");
        locadora.listarMaisAlugados();
    }

}

package exercicio;

public class User {

    private String name;
    private int idade;

    public User(String name, int idade) {
        this.name = name;
        this.idade = idade;
    }

    public String getName() {
        return name;
    }

    public int getIdade() {
        return idade;
    }
}

package exercicio;

import java.util.Scanner;

public class ex3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("type a positive int");
        int number = Integer.parseInt(scanner.nextLine());

        for (int i = number; i >= 0; i--) {
            System.out.println(i);
        }

    }

}

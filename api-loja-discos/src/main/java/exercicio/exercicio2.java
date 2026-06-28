package exercicio;

import java.util.Scanner;

public class exercicio2 {

    public static void main(String[] args) {

       Scanner scanner = new Scanner(System.in);

        System.out.println("What's ur month income?");
        double monthlyIncome = Double.parseDouble(scanner.nextLine());
        System.out.println("is your score bad?");
        boolean hasBadScore = Boolean.parseBoolean(scanner.nextLine());
        System.out.println("What's ur age?");
        int age = Integer.parseInt(scanner.nextLine());


        if (!hasBadScore && age >= 18 && age <= 65 && (monthlyIncome >=3000 || age >= 60 )) {
            System.out.println("loan approved");
        } else {
            System.out.println("loan denied");
        }


    }

}

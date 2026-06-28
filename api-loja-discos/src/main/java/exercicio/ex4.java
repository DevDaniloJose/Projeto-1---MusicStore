package exercicio;

import java.util.Scanner;

public class ex4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] arrayNames = new String[5];
        int[] arrayAges = new int[5];
        int count = 0;
        int choice;

        do {

            System.out.println("What'd u like to do?");
            System.out.println("\n1 - Register new user\n2 - List all Users\n3 - Leave system ");
            choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                if (count >= arrayNames.length) {
                    System.out.println("System full.");
                } else {
                    System.out.println("Enter name");
                    String name = scanner.nextLine();

                    System.out.println("Enter age");
                    int age = Integer.parseInt(scanner.nextLine());

                    arrayNames[count] = name;
                    arrayAges[count] = age;

                    count++;

                    System.out.println("User registered successfully!");

                }
            }

            if (choice == 2) {
                if (count == 0) {
                    System.out.println("No users registered yet");
                } else {
                    for (int i = 0; i < count; i++) {
                        System.out.println("name: " + arrayNames[i] + " age: " + arrayAges[i]);
                    }
                }
            }

        } while (choice != 3);
    }
}

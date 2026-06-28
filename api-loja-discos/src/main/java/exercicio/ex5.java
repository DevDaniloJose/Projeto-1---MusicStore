package exercicio;

import java.util.Scanner;

public class ex5 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] arrayName = new String[5];
        int[] arrayAge = new int[5];
        int choice;
        int count = 0;


        do {

            System.out.println("What'd u like to do?");
            System.out.println("\n1 - Register new user\n2 - List all Users\n3 - Leave system\n4 Find by name");
            choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                if (count >= arrayName.length) {
                    System.out.println("System full");
                } else {
                    System.out.println("Enter name");
                    String name = scanner.nextLine();
                    System.out.println("Enter age");
                    int age = Integer.parseInt(scanner.nextLine());

                    arrayName[count] = name;
                    arrayAge[count] = age;

                    count++;
                    System.out.println("User registered successfully");
                }

            }
            if (choice == 2) {
                if (count == 0) {
                    System.out.println("No users registered yet");
                } else {
                    for (int i = 0; i < count; i++) {
                        System.out.println("Name:" + arrayName[i] + " age: " + arrayAge[i]);
                    }
                }
            }

            if (choice == 4) {
                System.out.println("Type the name you want use to find");
                String nameInput = scanner.nextLine();
                String result = findName(nameInput, arrayName, count);

                System.out.println(result);
            }

        } while (choice != 3);
    }

    private static String findName(String name, String[] arrayName, int count) {
        if (count == 0) {
            System.out.println("No users registered yet");
        } else {
            for (int i = 0; i < count; i++) {
              if (arrayName[i].equalsIgnoreCase(name)) {
                  return "Index: " + i + " | Name: " + arrayName[i];
              }
            }
        }

        return "user not found";

    }


}

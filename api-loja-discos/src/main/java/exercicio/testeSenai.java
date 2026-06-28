package exercicio;

import java.util.ArrayList;
import java.util.Scanner;

public class testeSenai {

    public static void main(String[] args) {

        Scanner scanner =  new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();
        int choice;
 // \\\
        do {
            System.out.println("What would you like to do?");
            System.out.println("1 - Register new user\n2 - List all Users\n3 - leave system");
            choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.println("What's ur name?");
                String name = scanner.nextLine();
                System.out.println("What's ur age?");
                int age = Integer.parseInt(scanner.nextLine());

                User user = new User(name, age);
                users.add(user);
                System.out.println("User registered successfully");
            } else if (choice == 2) {
                System.out.println("Registered users:");
                if (users.isEmpty()) {
                    System.out.println("No users registered yet");
                } else {
                    for (User u : users) {
                        System.out.println("Name: " + u.getName() + " age: " + u.getIdade());
                    }
                }
            }



        } while (choice != 3);



    }

}

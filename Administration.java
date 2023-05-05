//this is all very placeholder still, but it's a start

import java.util.Scanner;

public class Administration {
    private final Scanner scanner;

    public Administration() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Administration module!");

        while (true) {
            System.out.println("Please select one of the following options:");
            System.out.println("1. a");
            System.out.println("2. b");
            System.out.println("3. c");
            System.out.println("4. d");
            System.out.println("5. e");
            System.out.println("6. f");
            System.out.println("7. g");
            System.out.println("Enter any other key to exit.");

            String choice = scanner.next();
            switch (choice) {
                case "1":
                    System.out.println("Option a selected.");
                    break;
                case "2":
                    System.out.println("Option b selected.");
                    break;
                case "3":
                    System.out.println("Option c selected.");
                    break;
                case "4":
                    System.out.println("Option d selected.");
                    break;
                case "5":
                    System.out.println("Option e selected.");
                    break;
                case "6":
                    System.out.println("Option f selected.");
                    break;
                case "7":
                    System.out.println("Option g selected.");
                    break;
                default:
                    System.out.println("Exiting Administration module...");
                    return;
            }
        }
    }
}

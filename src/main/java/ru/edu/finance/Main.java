package ru.edu.finance;

import ru.edu.finance.app.CommandRouter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandRouter router = new CommandRouter();

        System.out.println("Finance Manager started");
        System.out.println("Type 'help' to see available commands");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            boolean shouldContinue = router.handle(input);
            if (!shouldContinue) {
                break;
            }
        }

        scanner.close();
    }
}

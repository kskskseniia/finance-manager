package ru.edu.finance.app;

import java.util.Arrays;

public class CommandRouter {

    public boolean handle(String input) {
        String[] parts = input.trim().split("\\s+");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "help":
                printHelp();
                return true;

            case "exit":
                System.out.println("Goodbye!");
                return false;

            case "login":
                System.out.println("Login command (not implemented yet)");
                return true;

            case "register":
                System.out.println("Register command (not implemented yet)");
                return true;

            default:
                System.out.println("Unknown command. Type 'help'");
                return true;
        }
    }

    private void printHelp() {
        System.out.println("""
                Available commands:
                  register <login> <password>
                  login <login> <password>
                  help
                  exit
                """);
    }
}

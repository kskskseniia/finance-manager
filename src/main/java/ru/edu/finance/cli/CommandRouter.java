package ru.edu.finance.cli;

import ru.edu.finance.service.UserService;

public class CommandRouter {

    private final UserService authService = new UserService();

    public boolean handle(String input) {
        if (input == null || input.isBlank()) {
            System.out.println("Empty command. Type 'help'");
            return true;
        }

        String[] parts = input.trim().split("\\s+");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "help":
                printHelp();
                return true;

            case "register":
                handleRegister(parts);
                return true;

            case "login":
                handleLogin(parts);
                return true;

            case "logout":
                handleLogout();
                return true;

            case "users":
                handleUsers();
                return true;

            case "exit":
                System.out.println("Goodbye!");
                return false;

            default:
                System.out.println("Unknown command. Type 'help'");
                return true;
        }
    }

    private void handleRegister(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Usage: register <login> <password>");
            return;
        }

        boolean success = authService.register(parts[1], parts[2]);
        if (success) {
            System.out.println("User registered successfully");
        } else {
            System.out.println("Login already exists");
        }
    }

    private void handleLogin(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Usage: login <login> <password>");
            return;
        }

        boolean success = authService.login(parts[1], parts[2]);
        if (success) {
            System.out.println("Logged in as " + parts[1]);
        } else {
            System.out.println("Invalid login or password");
        }
    }

    private void handleLogout() {
        if (!authService.isAuthenticated()) {
            System.out.println("You are not logged in");
            return;
        }
        authService.logout();
        System.out.println("Logged out");
    }

    private void handleUsers() {
        var users = authService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No registered users");
            return;
        }

        System.out.println("Registered users:");
        users.keySet().forEach(login ->
                System.out.println(" - " + login)
        );
    }

    private void printHelp() {
        System.out.println("""
                Available commands:
                  register <login> <password>
                  login <login> <password>
                  logout
                  users
                  help
                  exit
                """);
    }
}

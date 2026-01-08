package ru.edu.finance.cli;

import ru.edu.finance.service.FinanceService;
import ru.edu.finance.service.UserService;

public class CommandRouter {

    private final UserService userService = new UserService();
    private final FinanceService financeService = new FinanceService();

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

            case "income":
                handleIncome(parts);
                return true;

            case "expense":
                handleExpense(parts);
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

        boolean success = userService.register(parts[1], parts[2]);
        System.out.println(success
                ? "User registered successfully"
                : "Login already exists");
    }

    private void handleLogin(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Usage: login <login> <password>");
            return;
        }

        boolean success = userService.login(parts[1], parts[2]);
        System.out.println(success
                ? "Logged in as " + parts[1]
                : "Invalid login or password");
    }

    private void handleLogout() {
        if (!userService.isAuthenticated()) {
            System.out.println("You are not logged in");
            return;
        }
        userService.logout();
        System.out.println("Logged out");
    }

    private void handleUsers() {
        var users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No registered users");
            return;
        }

        System.out.println("Registered users:");
        users.keySet().forEach(login ->
                System.out.println(" - " + login)
        );
    }

    private void handleIncome(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Please login first");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Usage: income <category> <amount>");
            return;
        }

        try {
            financeService.addIncome(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println("Income added");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleExpense(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Please login first");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Usage: expense <category> <amount>");
            return;
        }

        try {
            financeService.addExpense(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println("Expense added");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printHelp() {
        System.out.println("""
                Available commands:
                  register <login> <password>
                  login <login> <password>
                  logout
                  users
                  income <category> <amount>
                  expense <category> <amount>
                  help
                  exit
                """);
    }
}

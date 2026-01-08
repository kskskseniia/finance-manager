package ru.edu.finance.cli;

import ru.edu.finance.model.User;
import ru.edu.finance.service.FinanceService;
import ru.edu.finance.service.UserService;
import ru.edu.finance.service.StatisticsService;
import ru.edu.finance.service.TransferService;

import java.math.BigDecimal;
import java.util.List;

public class CommandRouter {

    private final UserService userService = new UserService();
    private final FinanceService financeService = new FinanceService();
    private final StatisticsService statisticsService = new StatisticsService();
    private final TransferService transferService =
            new TransferService(financeService, userService);

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

            case "transfer":
                handleTransfer(parts);
                return true;

            case "set-budget":
                handleSetBudget(parts);
                return true;

            case "budgets":
                handleBudgets();
                return true;

            case "stats":
                handleStats(parts);
                return true;

            case "exit":
                System.out.println("Goodbye!");
                return false;

            default:
                System.out.println("Unknown command. Type 'help'");
                return true;
        }
    }

    /* ================= AUTH ================= */

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

    /* ================= FINANCE ================= */

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

    private void handleTransfer(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Please login first");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Usage: transfer <toLogin> <amount>");
            return;
        }

        try {
            transferService.transfer(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println("Transfer completed");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleSetBudget(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Please login first");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Usage: set-budget <category> <limit>");
            return;
        }

        try {
            financeService.setBudget(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println("Budget set for category: " + parts[1]);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleBudgets() {
        if (!userService.isAuthenticated()) {
            System.out.println("Please login first");
            return;
        }

        var budgets = userService.getCurrentUser()
                .getWallet()
                .getBudgets();

        if (budgets.isEmpty()) {
            System.out.println("No budgets set");
            return;
        }

        System.out.println("Budgets:");
        budgets.values().forEach(budget -> {
            var spent = userService.getCurrentUser()
                    .getWallet()
                    .getTotalExpensesByCategory(budget.getCategory());

            var remaining = budget.getLimit().subtract(spent);

            System.out.println(
                    budget.getCategory()
                            + ": limit=" + budget.getLimit()
                            + ", remaining=" + remaining
            );
        });
    }

    /* ================= STATS ================= */

    private void handleStats(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Please login first");
            return;
        }

        var user = userService.getCurrentUser();

        if (parts.length == 1) {
            printGeneralStats(user);
            return;
        }

        if (parts.length == 3 && parts[1].equalsIgnoreCase("category")) {
            printSingleCategory(user, parts[2]);
            return;
        }

        if (parts.length == 3 && parts[1].equalsIgnoreCase("categories")) {
            printMultipleCategories(user, parts[2]);
            return;
        }

        System.out.println("Usage:");
        System.out.println("  stats");
        System.out.println("  stats category <name>");
        System.out.println("  stats categories <cat1,cat2,...>");
    }

    private void printGeneralStats(User user) {
        var totalIncome = statisticsService.getTotalIncome(user);
        var totalExpense = statisticsService.getTotalExpense(user);

        System.out.println("=== Statistics ===");

        System.out.println("Total income:  " + totalIncome);

        var incomeByCategory = statisticsService.getIncomeByCategory(user);
        if (!incomeByCategory.isEmpty()) {
            System.out.println("Income by category:");
            incomeByCategory.forEach((cat, sum) ->
                    System.out.println("  " + cat + ": " + sum)
            );
        }

        System.out.println("Total expense: " + totalExpense);

        var expenseByCategory = statisticsService.getExpensesByCategory(user);
        if (!expenseByCategory.isEmpty()) {
            System.out.println("Expenses by category:");
            expenseByCategory.forEach((cat, sum) ->
                    System.out.println("  " + cat + ": " + sum)
            );
        }
    }

    private void printSingleCategory(User user, String category) {
        var amount = statisticsService
                .getExpenseForCategories(user, List.of(category));

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("No expenses for category: " + category);
            return;
        }

        System.out.println("Expenses for " + category + ": " + amount);
    }

    private void printMultipleCategories(User user, String rawCategories) {
        var categories = List.of(rawCategories.split(","));

        var amount = statisticsService
                .getExpenseForCategories(user, categories);

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("No expenses for selected categories");
            return;
        }

        System.out.println("Expenses for categories " + categories + ": " + amount);
    }

    /* ================= HELP ================= */

    private void printHelp() {
        System.out.println("""
            ==================== Finance Manager ====================

            АУТЕНТИФИКАЦИЯ
              register <login> <password>     Регистрация нового пользователя
              login <login> <password>        Вход в систему
              logout                          Выход из текущей сессии
              users                           Показать всех пользователей (отладка)

            ФИНАНСЫ
              income <category> <amount>      Добавить доход
              expense <category> <amount>     Добавить расход
              transfer <login> <amount>       Перевод средств другому пользователю

            БЮДЖЕТЫ
              set-budget <category> <limit>   Установить бюджет для категории
              budgets                         Показать все бюджеты

            СТАТИСТИКА
              stats                           Общая статистика
              stats category <name>           Статистика по одной категории
              stats categories <a,b,c>        Статистика по нескольким категориям

            СИСТЕМА
              help                            Показать эту справку
              exit                            Выйти из приложения

            =========================================================
            """);
    }
}

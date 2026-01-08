package ru.edu.finance.cli;

import ru.edu.finance.model.User;
import ru.edu.finance.report.ReportFormat;
import ru.edu.finance.service.*;
import ru.edu.finance.storage.FileStorage;
import ru.edu.finance.util.Formatter;

import java.math.BigDecimal;
import java.util.List;

public class CommandRouter {

    private final UserService userService = new UserService();
    private final FinanceService financeService = new FinanceService();
    private final StatisticsService statisticsService = new StatisticsService();
    private final TransferService transferService =
            new TransferService(financeService, userService);
    private final ReportService reportService =
            new ReportService(statisticsService, new FileStorage());

    public boolean handle(String input) {
        if (input == null || input.isBlank()) {
            System.out.println("Пустая команда. Введите 'help' для справки");
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

            case "export":
                handleExport(parts);
                return true;

            case "exit":
                handleExit();
                return false;

            default:
                System.out.println("Неизвестная команда. Введите 'help'");
                return true;
        }
    }

    /* ================= АУТЕНТИФИКАЦИЯ ================= */

    private void handleRegister(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Использование: register <login> <password>");
            return;
        }

        boolean success = userService.register(parts[1], parts[2]);
        System.out.println(success
                ? "Пользователь успешно зарегистрирован"
                : "Пользователь с таким логином уже существует");
    }

    private void handleLogin(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Использование: login <login> <password>");
            return;
        }

        boolean success = userService.login(parts[1], parts[2]);
        System.out.println(success
                ? "Вход выполнен. Пользователь: " + parts[1]
                : "Неверный логин или пароль");
    }

    private void handleLogout() {
        if (!userService.isAuthenticated()) {
            System.out.println("Вы не вошли в систему");
            return;
        }
        userService.logout();
        System.out.println("Вы успешно вышли из системы");
    }

    private void handleUsers() {
        var users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("Пользователи не зарегистрированы");
            return;
        }

        System.out.println("Зарегистрированные пользователи:");
        users.keySet().forEach(login ->
                System.out.println(" - " + login)
        );
    }

    /* ================= ФИНАНСЫ ================= */

    private void handleIncome(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Сначала выполните вход");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Использование: income <категория> <сумма>");
            return;
        }

        try {
            financeService.addIncome(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println(
                    "Доход добавлен: категория = " + parts[1]
                            + ", сумма = " + Formatter.money(new BigDecimal(parts[2]))
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleExpense(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Сначала выполните вход");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Использование: expense <категория> <сумма>");
            return;
        }

        try {
            financeService.addExpense(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println(
                    "Расход добавлен: категория = " + parts[1]
                            + ", сумма = " + Formatter.money(new BigDecimal(parts[2]))
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleTransfer(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Сначала выполните вход");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Использование: transfer <login> <сумма>");
            return;
        }

        try {
            transferService.transfer(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println(
                    "Перевод выполнен: получатель = " + parts[1]
                            + ", сумма = " + Formatter.money(new BigDecimal(parts[2]))
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /* ================= БЮДЖЕТЫ ================= */

    private void handleSetBudget(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Сначала выполните вход");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Использование: set-budget <категория> <лимит>");
            return;
        }

        try {
            financeService.setBudget(
                    userService.getCurrentUser(),
                    parts[1],
                    parts[2]
            );
            System.out.println(
                    "Бюджет установлен: категория = "
                            + parts[1]
                            + ", лимит = "
                            + Formatter.money(new BigDecimal(parts[2]))
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleBudgets() {
        if (!userService.isAuthenticated()) {
            System.out.println("Сначала выполните вход");
            return;
        }

        var budgets = userService.getCurrentUser()
                .getWallet()
                .getBudgets();

        if (budgets.isEmpty()) {
            System.out.println("Бюджеты не заданы");
            return;
        }

        System.out.println("Бюджеты по категориям:");
        budgets.values().forEach(budget -> {
            var spent = userService.getCurrentUser()
                    .getWallet()
                    .getTotalExpensesByCategory(budget.getCategory());

            var remaining = budget.getLimit().subtract(spent);

            System.out.println(
                    "  " + budget.getCategory()
                            + ": лимит = " + Formatter.money(budget.getLimit())
                            + ", остаток = " + Formatter.money(remaining)
            );
        });
    }

    /* ================= СТАТИСТИКА ================= */

    private void handleStats(String[] parts) {
        if (!userService.isAuthenticated()) {
            System.out.println("Сначала выполните вход");
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

        System.out.println("Использование:");
        System.out.println("  stats");
        System.out.println("  stats category <категория>");
        System.out.println("  stats categories <a,b,c>");
    }

    private void printGeneralStats(User user) {
        var totalIncome = statisticsService.getTotalIncome(user);
        var totalExpense = statisticsService.getTotalExpense(user);

        System.out.println("========== Статистика ==========");

        System.out.println("Общий доход: "
                + Formatter.money(totalIncome));

        var incomeByCategory = statisticsService.getIncomeByCategory(user);
        if (!incomeByCategory.isEmpty()) {
            System.out.println("Доходы по категориям:");
            incomeByCategory.forEach((cat, sum) ->
                    System.out.println("  " + cat + ": " + Formatter.money(sum))
            );
        }

        System.out.println("Общие расходы: "
                + Formatter.money(totalExpense));

        var expenseByCategory = statisticsService.getExpensesByCategory(user);
        if (!expenseByCategory.isEmpty()) {
            System.out.println("Расходы по категориям:");
            expenseByCategory.forEach((cat, sum) ->
                    System.out.println("  " + cat + ": " + Formatter.money(sum))
            );
        }

        System.out.println("================================");
    }

    private void printSingleCategory(User user, String category) {
        var amount = statisticsService
                .getExpenseForCategories(user, List.of(category));

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Расходы по категории отсутствуют: " + category);
            return;
        }

        System.out.println(
                "Расходы по категории " + category + ": "
                        + Formatter.money(amount)
        );
    }

    private void printMultipleCategories(User user, String rawCategories) {
        var categories = List.of(rawCategories.split(","));

        var amount = statisticsService
                .getExpenseForCategories(user, categories);

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Расходы по выбранным категориям отсутствуют");
            return;
        }

        System.out.println(
                "Расходы по категориям " + categories + ": "
                        + Formatter.money(amount)
        );
    }

    private void handleExport(String[] parts) {

        if (!userService.isAuthenticated()) {
            System.out.println("Сначала выполните вход");
            return;
        }

        if (parts.length < 2 || !parts[1].equalsIgnoreCase("stats")) {
            System.out.println("Использование: export stats [json|csv]");
            return;
        }

        ReportFormat format = ReportFormat.JSON;

        if (parts.length == 3) {
            try {
                format = ReportFormat.valueOf(parts[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неизвестный формат. Доступно: json, csv");
                return;
            }
        }

        reportService.exportStatistics(userService.getCurrentUser(), format);

        System.out.println("Отчёт сохранён в формате " + format);
    }

    /* ================= EXIT ================= */

    private void handleExit() {
        if (userService.isAuthenticated()) {
            userService.logout();
            System.out.println("Данные пользователя сохранены");
        }
        System.out.println("Выход из приложения.");
    }


    /* ================= HELP ================= */

    private void printHelp() {
        System.out.println("""
            ==================== Finance Manager ====================

            АУТЕНТИФИКАЦИЯ
              register <login> <password>     Регистрация нового пользователя
              login <login> <password>        Вход в систему
              logout                          Выход из текущей сессии
              users                           (debug) Показать всех залогированных пользователей

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

            ОТЧЁТЫ
              export stats                    Экспорт статистики (JSON)
              export stats json               Экспорт статистики в JSON
              export stats csv                Экспорт статистики в CSV
            
            СИСТЕМА
              help                            Показать эту справку
              exit                            Выйти из приложения

            =========================================================
            """);
    }
}

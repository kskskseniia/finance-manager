package ru.edu.finance.service;

import ru.edu.finance.model.Transaction;
import ru.edu.finance.model.TransactionType;
import ru.edu.finance.model.User;
import ru.edu.finance.config.ConfigLoader;

import java.math.BigDecimal;

public class FinanceService {

    public void addIncome(User user, String category, String rawAmount) {
        BigDecimal amount = parseAndValidateAmount(rawAmount);

        Transaction transaction = new Transaction(
                TransactionType.INCOME,
                category,
                amount
        );

        user.getWallet().addTransaction(transaction);
    }

    public void addExpense(User user, String category, String rawAmount) {
        BigDecimal amount = parseAndValidateAmount(rawAmount);

        Transaction transaction = new Transaction(
                TransactionType.EXPENSE,
                category,
                amount
        );

        user.getWallet().addTransaction(transaction);
        checkBudget(user, category);
    }

    private BigDecimal parseAndValidateAmount(String rawAmount) {
        BigDecimal amount;

        try {
            amount = new BigDecimal(rawAmount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount must have at most 2 decimal places");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        return amount;
    }

    public void setBudget(User user, String category, String rawLimit) {
        BigDecimal limit = parseAndValidateAmount(rawLimit);
        user.getWallet().setBudget(category, limit);
    }

    public void checkBudget(User user, String category) {
        user.getWallet().getBudget(category).ifPresent(budget -> {

            BigDecimal spent = user.getWallet()
                    .getTotalExpensesByCategory(category);

            BigDecimal limit = budget.getLimit();
            BigDecimal warningLimit =
                    limit.multiply(ConfigLoader.getBudgetWarningThreshold());

            if (spent.compareTo(limit) > 0) {
                System.out.println(
                        "Budget exceeded for category: " + category
                );
            } else if (spent.compareTo(limit) == 0) {
                System.out.println(
                        "Budget fully used for category: " + category
                );
            } else if (spent.compareTo(warningLimit) >= 0) {
                System.out.println(
                        "Budget is almost exceeded ("
                                + ConfigLoader.getBudgetWarningThreshold()
                                .multiply(new BigDecimal("100"))
                                + "%) for category: " + category
                );
            }
        });
    }
}

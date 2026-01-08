package ru.edu.finance.service;

import ru.edu.finance.model.Transaction;
import ru.edu.finance.model.TransactionType;
import ru.edu.finance.model.User;

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
}

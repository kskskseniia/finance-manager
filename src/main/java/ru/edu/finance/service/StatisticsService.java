package ru.edu.finance.service;

import ru.edu.finance.model.Transaction;
import ru.edu.finance.model.TransactionType;
import ru.edu.finance.model.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {

    public BigDecimal getTotalIncome(User user) {
        BigDecimal sum = BigDecimal.ZERO;

        for (Transaction t : user.getWallet().getTransactions()) {
            if (t.getType() == TransactionType.INCOME) {
                sum = sum.add(t.getAmount());
            }
        }

        return sum;
    }

    public BigDecimal getTotalExpense(User user) {
        BigDecimal sum = BigDecimal.ZERO;

        for (Transaction t : user.getWallet().getTransactions()) {
            if (t.getType() == TransactionType.EXPENSE) {
                sum = sum.add(t.getAmount());
            }
        }

        return sum;
    }

    public Map<String, BigDecimal> getExpensesByCategory(User user) {
        Map<String, BigDecimal> result = new HashMap<>();

        for (Transaction t : user.getWallet().getTransactions()) {
            if (t.getType() == TransactionType.EXPENSE) {
                String category = t.getCategory();
                BigDecimal current = result.getOrDefault(category, BigDecimal.ZERO);
                result.put(category, current.add(t.getAmount()));
            }
        }

        return result;
    }

    public BigDecimal getExpenseForCategories(User user, List<String> categories) {
        BigDecimal sum = BigDecimal.ZERO;

        for (Transaction t : user.getWallet().getTransactions()) {
            if (t.getType() == TransactionType.EXPENSE &&
                    categories.contains(t.getCategory())) {
                sum = sum.add(t.getAmount());
            }
        }

        return sum;
    }

    public Map<String, BigDecimal> getIncomeByCategory(User user) {
        Map<String, BigDecimal> result = new HashMap<>();

        for (Transaction t : user.getWallet().getTransactions()) {
            if (t.getType() == TransactionType.INCOME) {
                String category = t.getCategory();
                BigDecimal current = result.getOrDefault(category, BigDecimal.ZERO);
                result.put(category, current.add(t.getAmount()));
            }
        }

        return result;
    }
}

package ru.edu.finance.model;

import java.math.BigDecimal;
import java.util.*;

public class Wallet {

    private final List<Transaction> transactions = new ArrayList<>();
    private final Map<String, Budget> budgets = new HashMap<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public void setBudget(String category, BigDecimal limit) {
        budgets.put(category, new Budget(category, limit));
    }

    public Optional<Budget> getBudget(String category) {
        return Optional.ofNullable(budgets.get(category));
    }

    public Map<String, Budget> getBudgets() {
        return Map.copyOf(budgets);
    }

    public BigDecimal getTotalExpensesByCategory(String category) {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

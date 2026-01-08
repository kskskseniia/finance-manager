package ru.edu.finance.model;

import java.math.BigDecimal;
import java.util.*;

public class Wallet {

    private List<Transaction> transactions;
    private Map<String, Budget> budgets;

    // ОБЯЗАТЕЛЬНО для Jackson
    public Wallet() {
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setBudget(String category, BigDecimal limit) {
        budgets.put(category, new Budget(category, limit));
    }

    public Optional<Budget> getBudget(String category) {
        return Optional.ofNullable(budgets.get(category));
    }

    public Map<String, Budget> getBudgets() {
        return budgets;
    }

    public BigDecimal getTotalExpensesByCategory(String category) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE
                    && t.getCategory().equalsIgnoreCase(category)) {
                sum = sum.add(t.getAmount());
            }
        }
        return sum;
    }
}

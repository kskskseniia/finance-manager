package ru.edu.finance.model;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }
}

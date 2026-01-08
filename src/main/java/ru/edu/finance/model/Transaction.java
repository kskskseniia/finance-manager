package ru.edu.finance.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Transaction {

    private final TransactionType type;
    private final String category;
    private final BigDecimal amount;
    private final LocalDateTime dateTime;

    public Transaction(TransactionType type, String category, BigDecimal amount) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
    }

    public TransactionType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

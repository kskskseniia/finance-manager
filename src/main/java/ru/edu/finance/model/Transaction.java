package ru.edu.finance.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private TransactionType type;
    private String category;
    private BigDecimal amount;
    private LocalDateTime dateTime;

    public Transaction() {
    }

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

package ru.edu.finance.report;

import java.math.BigDecimal;
import java.util.Map;

public class StatisticsReport {

    private String login;

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;

    private Map<String, BigDecimal> incomeByCategory;
    private Map<String, BigDecimal> expenseByCategory;

    public StatisticsReport() {
    }

    public StatisticsReport(
            String login,
            BigDecimal totalIncome,
            BigDecimal totalExpense,
            Map<String, BigDecimal> incomeByCategory,
            Map<String, BigDecimal> expenseByCategory
    ) {
        this.login = login;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.incomeByCategory = incomeByCategory;
        this.expenseByCategory = expenseByCategory;
    }

    public String getLogin() {
        return login;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public Map<String, BigDecimal> getIncomeByCategory() {
        return incomeByCategory;
    }

    public Map<String, BigDecimal> getExpenseByCategory() {
        return expenseByCategory;
    }
}

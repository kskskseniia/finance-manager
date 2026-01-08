package ru.edu.finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.edu.finance.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {

    private StatisticsService statisticsService;
    private FinanceService financeService;
    private User user;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsService();
        financeService = new FinanceService();
        user = new User("test", "123");

        // подготовка данных: доходы
        financeService.addIncome(user, "Зарплата", "1000");
        financeService.addIncome(user, "Зарплата", "500");
        financeService.addIncome(user, "Бонус", "300");

        // подготовка данных: расходы
        financeService.addExpense(user, "Еда", "200");
        financeService.addExpense(user, "Еда", "100");
        financeService.addExpense(user, "Такси", "150");
    }

    // проверка подсчёта общего дохода пользователя
    @Test
    void getTotalIncome_correctSumReturned() {
        BigDecimal totalIncome = statisticsService.getTotalIncome(user);

        assertEquals(new BigDecimal("1800"), totalIncome);
    }

    // проверка подсчёта общего расхода пользователя
    @Test
    void getTotalExpense_correctSumReturned() {
        BigDecimal totalExpense = statisticsService.getTotalExpense(user);

        assertEquals(new BigDecimal("450"), totalExpense);
    }

    // проверка агрегации доходов по категориям
    @Test
    void getIncomeByCategory_groupedCorrectly() {
        Map<String, BigDecimal> incomeByCategory =
                statisticsService.getIncomeByCategory(user);

        assertEquals(2, incomeByCategory.size());
        assertEquals(new BigDecimal("1500"), incomeByCategory.get("Зарплата"));
        assertEquals(new BigDecimal("300"), incomeByCategory.get("Бонус"));
    }

    // проверка агрегации расходов по категориям
    @Test
    void getExpensesByCategory_groupedCorrectly() {
        Map<String, BigDecimal> expenseByCategory =
                statisticsService.getExpensesByCategory(user);

        assertEquals(2, expenseByCategory.size());
        assertEquals(new BigDecimal("300"), expenseByCategory.get("Еда"));
        assertEquals(new BigDecimal("150"), expenseByCategory.get("Такси"));
    }

    // проверка подсчёта расходов по одной выбранной категории
    @Test
    void getExpenseForSingleCategory_correctSumReturned() {
        BigDecimal amount = statisticsService
                .getExpenseForCategories(user, List.of("Еда"));

        assertEquals(new BigDecimal("300"), amount);
    }

    // проверка подсчёта расходов по нескольким выбранным категориям
    @Test
    void getExpenseForMultipleCategories_correctSumReturned() {
        BigDecimal amount = statisticsService
                .getExpenseForCategories(user, List.of("Еда", "Такси"));

        assertEquals(new BigDecimal("450"), amount);
    }

    // проверка поведения при отсутствии выбранных категорий
    @Test
    void getExpenseForUnknownCategory_returnsZero() {
        BigDecimal amount = statisticsService
                .getExpenseForCategories(user, List.of("Развлечения"));

        assertEquals(BigDecimal.ZERO, amount);
    }
}

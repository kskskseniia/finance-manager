package ru.edu.finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.edu.finance.model.TransactionType;
import ru.edu.finance.model.User;

import static org.junit.jupiter.api.Assertions.*;

class FinanceServiceTest {

    private FinanceService financeService;
    private User user;

    @BeforeEach
    void setUp() {
        financeService = new FinanceService();
        user = new User("test", "123");
    }

    // проверка добавления дохода с корректной суммой
    @Test
    void addIncome_validAmount_transactionAdded() {
        financeService.addIncome(user, "Salary", "1000");

        assertEquals(1, user.getWallet().getTransactions().size());
        assertEquals(TransactionType.INCOME,
                user.getWallet().getTransactions().get(0).getType());
    }

    // проверка добавления расхода с корректной суммой
    @Test
    void addExpense_validAmount_transactionAdded() {
        financeService.addExpense(user, "Food", "200");

        assertEquals(1, user.getWallet().getTransactions().size());
        assertEquals(TransactionType.EXPENSE,
                user.getWallet().getTransactions().get(0).getType());
    }

    // проверка валидации суммы с более чем двумя знаками после запятой
    @Test
    void addIncome_amountWithTooManyDecimals_rejected() {
        assertThrows(IllegalArgumentException.class, () ->
                financeService.addIncome(user, "Salary", "10.999"));
    }

    // проверка валидации отрицательной суммы
    @Test
    void addExpense_negativeAmount_rejected() {
        assertThrows(IllegalArgumentException.class, () ->
                financeService.addExpense(user, "Food", "-100"));
    }

    // проверка валидации нулевой суммы
    @Test
    void addIncome_zeroAmount_rejected() {
        assertThrows(IllegalArgumentException.class, () ->
                financeService.addIncome(user, "Salary", "0"));
    }
}

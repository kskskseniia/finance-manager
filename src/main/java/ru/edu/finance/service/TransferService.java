package ru.edu.finance.service;

import ru.edu.finance.model.Transaction;
import ru.edu.finance.model.TransactionType;
import ru.edu.finance.model.User;

import java.math.BigDecimal;

public class TransferService {

    private final FinanceService financeService;
    private final UserService userService;

    public TransferService(FinanceService financeService,
                           UserService userService) {
        this.financeService = financeService;
        this.userService = userService;
    }

    public void transfer(User from, String toLogin, String rawAmount) {
        User to = userService.findOrLoadUser(toLogin);

        if (to == null) {
            throw new IllegalArgumentException("User not found: " + toLogin);
        }

        // расход у отправителя
        financeService.addExpense(from, "TRANSFER", rawAmount);

        // доход у получателя
        financeService.addIncome(to, "TRANSFER", rawAmount);

        // сохраняем получателя
        userService.saveUser(to);
    }
}

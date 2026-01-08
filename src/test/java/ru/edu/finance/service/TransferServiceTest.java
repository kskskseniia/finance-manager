package ru.edu.finance.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.edu.finance.config.ConfigLoader;
import ru.edu.finance.model.User;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferServiceTest {

    private UserService userService;
    private FinanceService financeService;
    private TransferService transferService;

    // очистка файлового хранилища
    private void cleanDataDir() throws Exception {
        Path dataDir = Path.of(ConfigLoader.storageDir());

        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (Exception ignored) {
                        }
                    });
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        cleanDataDir();

        userService = new UserService();
        financeService = new FinanceService();
        transferService = new TransferService(financeService, userService);

        // подготовка пользователей
        userService.register("A", "1");
        userService.register("B", "2");
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanDataDir();
    }

    // проверка перевода средств между пользователями
    @Test
    void transfer_validUsers_moneyTransferred() {
        // вход отправителя
        userService.login("A", "1");

        // доход отправителю
        financeService.addIncome(
                userService.getCurrentUser(),
                "Salary",
                "100"
        );

        // перевод средств получателю
        transferService.transfer(
                userService.getCurrentUser(),
                "B",
                "50"
        );

        User userA = userService.getCurrentUser();
        User userB = userService.findOrLoadUser("B");

        // у отправителя: доход + расход
        assertEquals(
                2,
                userA.getWallet().getTransactions().size()
        );

        // у получателя: доход от перевода
        assertEquals(
                1,
                userB.getWallet().getTransactions().size()
        );
    }
}

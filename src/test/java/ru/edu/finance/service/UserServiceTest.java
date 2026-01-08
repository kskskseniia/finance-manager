package ru.edu.finance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    // проверка успешной регистрации нового пользователя
    @Test
    void register_newUser_created() {
        boolean result = userService.register("test", "123");

        assertTrue(result);
    }

    // проверка запрета регистрации с уже существующим логином
    @Test
    void register_existingLogin_rejected() {
        userService.register("test", "123");

        boolean result = userService.register("test", "456");

        assertFalse(result);
    }

    // проверка успешного входа с корректным логином и паролем
    @Test
    void login_validCredentials_success() {
        userService.register("test", "123");

        boolean loggedIn = userService.login("test", "123");

        assertTrue(loggedIn);
        assertNotNull(userService.getCurrentUser());
    }

    // проверка отказа во входе при неверном пароле
    @Test
    void login_invalidPassword_rejected() {
        userService.register("test", "123");

        boolean loggedIn = userService.login("test", "wrong");

        assertFalse(loggedIn);
        assertNull(userService.getCurrentUser());
    }
}

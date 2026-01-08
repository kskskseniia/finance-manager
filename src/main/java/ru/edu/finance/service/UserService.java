package ru.edu.finance.service;

import ru.edu.finance.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private User currentUser;

    public boolean register(String login, String password) {
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, new User(login, password));
        return true;
    }

    public boolean login(String login, String password) {
        User user = users.get(login);
        if (user == null || !user.checkPassword(password)) {
            return false;
        }
        currentUser = user;
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Map<String, User> getAllUsers() {
        return Map.copyOf(users);
    }
}

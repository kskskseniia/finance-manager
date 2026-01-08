package ru.edu.finance.service;

import ru.edu.finance.model.User;
import ru.edu.finance.storage.FileStorage;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final Map<String, User> users = new HashMap<>();
    private User currentUser;
    private final FileStorage storage = new FileStorage();

    public boolean register(String login, String password) {
        if (users.containsKey(login)) {
            return false;
        }

        if (storage.userExists(login)) {
            return false;
        }

        users.put(login, new User(login, password));
        return true;
    }

    public boolean login(String login, String password) {
        if (currentUser != null) {
            logout();
        }

        User user = findOrLoadUser(login);

        if (user == null) {
            user = storage.loadUser(login);
            if (user != null) {
                users.put(login, user);
            }
        }

        if (user == null || !user.checkPassword(password)) {
            return false;
        }

        currentUser = user;
        return true;
    }

    public void logout() {
        if (currentUser != null) {
            storage.saveUser(currentUser);
            currentUser = null;
        }
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

    public User findUser(String login) {
        return users.get(login);
    }

    public User findOrLoadUser(String login) {
        User user = users.get(login);
        if (user == null) {
            user = storage.loadUser(login);
            if (user != null) {
                users.put(login, user);
            }
        }
        return user;
    }

    public void saveUser(User user) {
        storage.saveUser(user);
    }
}

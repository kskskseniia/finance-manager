package ru.edu.finance.model;

public class User {

    private final String login;
    private final String password;
    private final Wallet wallet;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.wallet = new Wallet();
    }

    public String getLogin() {
        return login;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Wallet getWallet() {
        return wallet;
    }
}
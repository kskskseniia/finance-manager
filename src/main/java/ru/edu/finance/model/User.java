package ru.edu.finance.model;

public class User {

    private String login;
    private String password;
    private Wallet wallet;

    public User() {
        this.wallet = new Wallet();
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.wallet = new Wallet();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public boolean checkPassword(String rawPassword) {
        return password != null && password.equals(rawPassword);
    }
}
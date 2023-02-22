package com.mindhub.homebanking.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private TransactionType type;

    @Column(precision = 10, scale = 2)
    private double amount;

    private String description;

    private LocalDateTime date;

    @ManyToOne
    private Account account;

    public Transaction() {
    }

    public Transaction(TransactionType type, double amount, String description, Account account) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.account = account;
        this.date = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

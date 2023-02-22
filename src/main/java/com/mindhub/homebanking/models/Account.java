package com.mindhub.homebanking.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String number;
    private LocalDateTime creationDate;
    @Column(precision=10, scale=2)
    private double balance;
    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Transaction> transactions= new ArrayList<>();

    public Account(){}

    public Account(String number, LocalDateTime creationDate, double balance, Client client){
        this.number=number;
        this.creationDate= creationDate;
        this.balance=balance;
        this.client=client;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}

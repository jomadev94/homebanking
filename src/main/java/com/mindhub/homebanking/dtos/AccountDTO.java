package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;
    private String number;
    private double balance;
    private LocalDateTime creationDate;

    private List<TransactionDTO> transactions;

    public AccountDTO(){}

    public AccountDTO(Account account){
        this.id=account.getId();
        this.number=account.getNumber();
        this.balance=account.getBalance();
        this.creationDate= account.getCreationDate();
        this.transactions=account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}

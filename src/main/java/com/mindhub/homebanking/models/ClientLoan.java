package com.mindhub.homebanking.models;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double amount;
    private int payments;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Loan loan;

    public ClientLoan(){}

    public ClientLoan(double amount, int payments, Client client, Loan loan) {
        this.amount = amount;
        this.payments = payments;
        this.client = client;
        this.loan = loan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}

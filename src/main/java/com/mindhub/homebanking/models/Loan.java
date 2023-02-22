package com.mindhub.homebanking.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private double maxAmount;
    @ElementCollection
    private Set<Integer> payments;
    @OneToMany(mappedBy = "loan", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<ClientLoan> client;

    public Loan(){}

    public Loan(String name, double maxAmount, Set<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Set<Integer> getPayments() {
        return payments;
    }

    public void setPayments(Set<Integer> payments) {
        this.payments = payments;
    }

    public List<ClientLoan> getClients() {
        return client;
    }

    public void setClients(List<ClientLoan> clients) {
        this.client = clients;
    }
}

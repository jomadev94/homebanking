package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountTransactionDTO;
import com.mindhub.homebanking.exceptions.ForbiddenException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void create(Authentication auth, AccountTransactionDTO transaction) {
        Client current = clientRepository.findByEmail(auth.getName());
        if (transaction.getTo().equals(transaction.getFrom())) throw new ForbiddenException("Account from and Account to must be different");
        Account from = accountRepository.findByNumber(transaction.getFrom());
        Account to = accountRepository.findByNumber(transaction.getTo());
        if (from == null || to == null) throw new ForbiddenException("Account from or to not exist");
        if (from.getClient().getId() != current.getId()) throw new ForbiddenException("Not account owner");
        if (from.getBalance() < transaction.getAmount()) throw new ForbiddenException("You don't have enough balance");
        Transaction t1 = new Transaction(TransactionType.DEBIT, -transaction.getAmount(), transaction.getDescription(), from);
        Transaction t2 = new Transaction(TransactionType.CREDIT, transaction.getAmount(), transaction.getDescription(), to);
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        from.setBalance(from.getBalance() - transaction.getAmount());
        to.setBalance(to.getBalance() + transaction.getAmount());
        accountRepository.flush();
    }
}

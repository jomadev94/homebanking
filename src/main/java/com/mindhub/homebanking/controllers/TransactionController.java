package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountTransactionDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/transaction")
    @Transactional
    public ResponseEntity<?> accountTransaction(@RequestBody AccountTransactionDTO transaction, Authentication auth) {
        Client current = clientRepository.findByEmail(auth.getName());
        if (transaction.getAmount() <= 0 ||
                transaction.getDescription().isEmpty() ||
                transaction.getFrom().isEmpty() ||
                transaction.getTo().isEmpty())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (transaction.getTo().equals(transaction.getFrom())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Account from = accountRepository.findByNumber(transaction.getFrom());
        Account to=accountRepository.findByNumber(transaction.getTo());
        if (from == null || to == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (from.getClient().getId() != current.getId()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if(from.getBalance() < transaction.getAmount()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Transaction t1= new Transaction(TransactionType.DEBIT,-transaction.getAmount(),transaction.getDescription(),from);
        Transaction t2= new Transaction(TransactionType.CREDIT,transaction.getAmount(),transaction.getDescription(),to);
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        from.setBalance(from.getBalance() - transaction.getAmount());
        to.setBalance(to.getBalance() + transaction.getAmount());
        accountRepository.flush();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

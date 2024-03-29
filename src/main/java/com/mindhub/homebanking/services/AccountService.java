package com.mindhub.homebanking.services;

import com.mindhub.homebanking.exceptions.ForbiddenException;
import com.mindhub.homebanking.exceptions.NotFoundException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    public Account getAccount(long id){
        Optional<Account> account=accountRepository.findById(id);
        if(account.isEmpty()) throw new NotFoundException("Account not exist");
        return account.get();
    }

    public Account create(Authentication auth){
        Client current=clientRepository.findByEmail(auth.getName()).orElseThrow(()-> new NotFoundException("Client not found"));
        if(current.getAccounts().size() == 3){
            throw new ForbiddenException("The maximum number of accounts allowed was reached");
        }
        return accountRepository.save(new Account("VIN"+ ThreadLocalRandom.current().nextInt(10000,99999999), LocalDateTime.now(),10000.0,current));
    }


}

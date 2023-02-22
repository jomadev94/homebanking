package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountRepository.findById(id).map(AccountDTO::new).get();
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication auth){
        if(auth == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Client current=clientRepository.findByEmail(auth.getName());
        if(current.getAccounts().size() == 3){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Account created = accountRepository.save(new Account("VIN"+ ThreadLocalRandom.current().nextInt(10000,99999999), LocalDateTime.now(),0.0,current));
        URI location= ServletUriComponentsBuilder.fromUriString("/api/accounts/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(new AccountDTO(created));
    }


}

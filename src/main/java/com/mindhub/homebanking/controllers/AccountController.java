package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ResponseDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.AccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Account")
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<ResponseDTO> getAccounts() {
        List<AccountDTO> accounts = accountService.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseDTO(accounts), HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<ResponseDTO> getAccount(@PathVariable long id) {
        Account account = accountService.getAccount(id);
        return new ResponseEntity<>(new ResponseDTO(new AccountDTO(account)), HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<ResponseDTO> createAccount(Authentication auth) {
        Account created = accountService.create(auth);
//        URI location = ServletUriComponentsBuilder.fromUriString("/api/accounts/{id}").buildAndExpand(created.getId()).toUri();
//        return ResponseEntity.created(location).body(new ResponseDTO(201, created));
        ResponseDTO response=new ResponseDTO(201,created);
        return new ResponseEntity<>(new ResponseDTO(201,new AccountDTO(created)),HttpStatus.CREATED);
    }


}

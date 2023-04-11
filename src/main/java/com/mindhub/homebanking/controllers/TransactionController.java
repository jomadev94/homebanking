package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountTransactionDTO;
import com.mindhub.homebanking.services.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;

@RestController
@Api(tags = "Transaction")
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    @Transactional
    public ResponseEntity<?> accountTransaction(@RequestBody AccountTransactionDTO transaction, @ApiIgnore Authentication auth) {
        transactionService.create(auth, transaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CreateCardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCard(Authentication auth, @RequestBody CreateCardDTO card) {
        if (auth == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Client current = clientRepository.findByEmail(auth.getName());
        if (current.getCards().stream().filter(c -> c.getType().equals(card.getType())).count() == 3) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        StringBuilder cardNumber= new StringBuilder();
        for(int i=0; i<4; i++){
            Integer randomNumber = (Integer)ThreadLocalRandom.current().nextInt(1000,9999);
            cardNumber.append(randomNumber.toString());
        }
        Card created=cardRepository.save(new Card(current.getFirstName() + " " + current.getLastName(),
                card.getType(), card.getColor(),
                cardNumber.toString(), ThreadLocalRandom.current().nextInt(100,999),
                LocalDateTime.now().plusYears(5), LocalDateTime.now(), current));
        return new ResponseEntity<>(new CardDTO(created),HttpStatus.CREATED);
    }
}

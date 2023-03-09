package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CreateCardDTO;
import com.mindhub.homebanking.exceptions.ForbiddenException;
import com.mindhub.homebanking.exceptions.NotFoundException;
import com.mindhub.homebanking.exceptions.UnauthorizedException;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class CardService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    public Card create(Authentication auth, CreateCardDTO card) {
        Client current = clientRepository.findByEmail(auth.getName()).orElseThrow(()-> new NotFoundException("Client not found"));
        if (current.getCards().stream().filter(c -> c.getType().equals(card.getType())).count() == 3)
            throw new ForbiddenException("The maximum number of cards allowed was reached");
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            Integer randomNumber = (Integer) ThreadLocalRandom.current().nextInt(1000, 9999);
            cardNumber.append(randomNumber.toString());
        }
        return cardRepository.save(new Card(current.getFirstName() + " " + current.getLastName(),
                card.getType(), card.getColor(),
                cardNumber.toString(), ThreadLocalRandom.current().nextInt(100, 999),
                LocalDateTime.now().plusYears(5), LocalDateTime.now(), current));
    }
}

package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.exceptions.BadReqException;
import com.mindhub.homebanking.exceptions.ForbiddenException;
import com.mindhub.homebanking.exceptions.NotFoundException;
import com.mindhub.homebanking.exceptions.UnauthorizedException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Client getClient(long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) throw new NotFoundException("Client not exist");
        return client.get();
    }

    public Client getCurrent(Authentication auth) {
        if (auth == null) throw new UnauthorizedException();
        Client current = clientRepository.findByEmail(auth.getName());
        if (current == null) throw new UnauthorizedException();
        return current;
    }

    public Client register(RegisterDTO client) {
        if (clientRepository.findByEmail(client.getEmail()) != null)
            throw new ForbiddenException("Email already in use");
        Client created = clientRepository.save(new Client(client.getFirstName(), client.getLastName(), client.getEmail(), passwordEncoder.encode(client.getPassword())));
        accountRepository.save(new Account("VIN" + ThreadLocalRandom.current().nextInt(10000, 99999999), LocalDateTime.now(), 0.0, created));
        return created;
    }

    public void update(long id, ClientDTO modified) {
        if (id != modified.getId()) throw new BadReqException("Ids not match");
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) throw new NotFoundException("Client not exists");
        client.get().setFirstName(modified.getFirstName());
        client.get().setLastName(modified.getLastName());
        client.get().setEmail(modified.getEmail());
        clientRepository.flush();
    }

    public void delete(long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) throw new NotFoundException("Client not exists");
        clientRepository.delete(client.get());
    }


}

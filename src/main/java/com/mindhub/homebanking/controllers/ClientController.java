package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication auth) {
        if(auth == null) return null;
        Client current=clientRepository.findByEmail(auth.getName());
        return current != null? new ClientDTO(current):null;
    }

    @PostMapping("/clients")
    public ResponseEntity<?> createClient(@RequestBody RegisterDTO registerDTO) {
        if(registerDTO.getEmail().isEmpty() || registerDTO.getPassword().isEmpty() || registerDTO.getFirstName().isEmpty() || registerDTO.getLastName().isEmpty())
            return new ResponseEntity<>("Missing data!",HttpStatus.FORBIDDEN);
        if(clientRepository.findByEmail(registerDTO.getEmail()) != null)
            return new ResponseEntity<>("Email already in use",HttpStatus.FORBIDDEN);
        Client created=clientRepository.save(new Client(registerDTO.getFirstName(), registerDTO.getLastName(), registerDTO.getEmail(),passwordEncoder.encode(registerDTO.getPassword())));
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        accountRepository.save(new Account("VIN"+ ThreadLocalRandom.current().nextInt(10000,99999999), LocalDateTime.now(),0.0,created));
        return ResponseEntity.created(location).body(new ClientDTO(created));
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<?> updateClient(@PathVariable long id, @RequestBody ClientDTO update) {
        if(id != update.getId()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Client> client=clientRepository.findById(id);
        if(client.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        client.get().setFirstName(update.getFirstName());
        client.get().setLastName(update.getLastName());
        client.get().setEmail(update.getEmail());
        clientRepository.flush();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> updateClient(@PathVariable long id) {
        Optional<Client> client=clientRepository.findById(id);
        if(client.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        clientRepository.delete(client.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

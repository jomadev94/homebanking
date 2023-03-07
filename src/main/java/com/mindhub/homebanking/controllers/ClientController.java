package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.dtos.ResponseDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Api(tags = "Client")
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Get clients")
    @GetMapping("/clients")
    public ResponseEntity<ResponseDTO> getClients() {
        List<ClientDTO> clients = clientService.getClients().stream().map(ClientDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseDTO(clients), HttpStatus.OK);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ResponseDTO> getClient(@PathVariable long id) {
        ClientDTO client = new ClientDTO(clientService.getClient(id));
        return new ResponseEntity<>(new ResponseDTO(client), HttpStatus.OK);
    }

    @GetMapping("/clients/current")
    public ResponseEntity<ResponseDTO> getCurrentClient(Authentication auth) {
        Client current = clientService.getCurrent(auth);
        return new ResponseEntity<>(new ResponseDTO(new ClientDTO(current)), HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        Client client = clientService.register(registerDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(location).body(new ResponseDTO(201, new ClientDTO(client)));
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<ResponseDTO> updateClient(@PathVariable long id, @RequestBody ClientDTO update) {
        clientService.update(id, update);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable long id) {
        clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

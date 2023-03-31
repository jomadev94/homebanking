package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.*;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.swagger.web.SecurityConfiguration;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@Api(tags = "Client")
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Get clients")
    @GetMapping
    public ResponseEntity<ResponseDTO> getClients() {
        List<ClientDTO> clients = clientService.getClients().stream().map(ClientDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseDTO(clients), HttpStatus.OK);
    }

    @Operation(summary = "Get clients by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getClient(@PathVariable long id) {
        ClientDTO client = new ClientDTO(clientService.getClient(id));
        return new ResponseEntity<>(new ResponseDTO(client), HttpStatus.OK);
    }

    @Operation(summary = "Get current clients")
    @GetMapping("/current")
    public ResponseEntity<ResponseDTO> getCurrentClient(@ApiIgnore Authentication auth) {
        Client current = clientService.getCurrent(auth);
        return new ResponseEntity<>(new ResponseDTO(new ClientDTO(current)), HttpStatus.OK);
    }

    @Operation(summary = "Register")
    @Transactional
    @PostMapping
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        Client client = clientService.register(registerDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(location).body(new ResponseDTO(201, new ClientDTO(client)));
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO){
        String token= clientService.login(loginDTO);
        Client client= clientService.getClient(loginDTO.getEmail());
        LoginResponseDto response=new LoginResponseDto(token,client);
        return new ResponseEntity<>(new ResponseDTO(response), HttpStatus.OK);
    }

    @Operation(summary="Verify account")
    @PostMapping("/{code}/verify")
    public ResponseEntity<ResponseDTO> verify(@PathVariable String code,@ApiIgnore Authentication auth){
        clientService.verify(auth,code);
        return new ResponseEntity<>(new ResponseDTO(),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateClient(@PathVariable long id, @RequestBody ClientDTO update) {
        clientService.update(id, update);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable long id) {
        clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ClientService clientService;

    private static List<Client> clients;

    @BeforeAll
    static void setUp() {
        Client client1 = new Client("pedro", "sanchez", "pedro@gmail.com", "1234");
        client1.setId(1);
        Client client2 = new Client("marta", "rodriguez", "marta@hotmail.com", "5678");
        client2.setId(2);
        Client client3 = new Client("oscar", "figeroa", "oscar@gmail.com", "9876");
        client3.setId(3);
        clients = List.of(client1, client2, client3);
    }

    @Test
    @DisplayName("Get all clients")
    void getClients() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<Client> result = clientService.getClients();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    @DisplayName("Get client by Id")
    void getClient() {
        Optional<Client> client = clients.stream().filter(c -> c.getId() == 2).findFirst();
        when(clientRepository.findById(any(Long.class))).thenReturn(client);
        Client result = clientService.getClient(1);
        assertEquals("marta@hotmail.com", result.getEmail());
    }

    @Test
    @DisplayName("Get current client - without auth")
    void getCurrentNoAuth() {
        assertThrows(UnauthorizedException.class, () -> clientService.getCurrent(null));
    }

    @Test
    @DisplayName("Create a client - email already in use")
    void registerEmailExist() {
        when(clientRepository.findByEmail(anyString())).thenReturn(clients.get(1));
        assertThrows(ForbiddenException.class, () -> clientService.register(new RegisterDTO("Sebastian", "Perez", "seba@gmail.com", "Sarasa123")));
    }

    @Test
    @DisplayName("Create a client")
    void register() {
        when(clientRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("325$6732t123aADw");
        when(clientRepository.save(any(Client.class))).thenReturn(clients.get(0));
        when(accountRepository.save(any(Account.class))).thenReturn(null);
        Client result = clientService.register(new RegisterDTO(clients.get(0)));
        assertEquals(result.getEmail(), "pedro@gmail.com");
    }

    @Test
    @DisplayName("Update client - Not match id")
    void updateNotMatch() {
        assertThrows(BadReqException.class, () -> clientService.update(1, new ClientDTO(clients.get(1))));
    }

    @Test
    @DisplayName("Update client - Not found client")
    void updateNotFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> clientService.update(2, new ClientDTO(clients.get(1))));
    }

    @Test
    @DisplayName("Delete client - Not found client")
    void delete() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> clientService.delete(2));
    }
}
package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.exceptions.BadReqException;
import com.mindhub.homebanking.exceptions.ForbiddenException;
import com.mindhub.homebanking.exceptions.NotFoundException;
import com.mindhub.homebanking.exceptions.UnauthorizedException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Role;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private EmailSenderService emailSenderService;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Client getClient(long id) {
        return clientRepository.findById(id).orElseThrow(()->new NotFoundException("Client not exist"));
    }

    public Client getClient(String email){
        return clientRepository.findByEmail(email).orElseThrow(()->new NotFoundException("Client not exist"));
    }

    public Client getCurrent(Authentication auth) {
        return clientRepository.findByEmail(auth.getName()).orElseThrow(UnauthorizedException::new);
    }

    public Client register(RegisterDTO client) {
        if (clientRepository.existsByEmail(client.getEmail()))
            throw new ForbiddenException("Email already in use");
        Client created=new Client(client.getFirstName(), client.getLastName(), client.getEmail(), passwordEncoder.encode(client.getPassword()), Role.UNVERIFIED);
        clientRepository.save(created);
        accountRepository.save(new Account("VIN" + ThreadLocalRandom.current().nextInt(10000, 99999999), LocalDateTime.now(), 0.0, created));
        String html="<h1>Welcome to Homebanking</h1>" +
                "<p>In order to use our service you must verify your account through this <a href='http://localhost:5173/"+created.getCode().toString()+"/verify'>link</a></p>";
        emailSenderService.sendEmailHtml(client.getEmail(),"Client validation",html);
        return created;
    }

    public void verify(Authentication auth,String code){
        Client current=clientRepository.findByEmail(auth.getName()).orElseThrow(UnauthorizedException::new);
        if(!current.getCode().toString().equals(code)) throw new UnauthorizedException();
        current.setRole(Role.CLIENT);
        clientRepository.flush();
    }

    public String login(LoginDTO loginDTO){
        Authentication auth= authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return tokenUtils.generateToken(auth);
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

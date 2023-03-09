package com.mindhub.homebanking.services;

import com.mindhub.homebanking.exceptions.NotFoundException;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client=clientRepository.findByEmail(username).orElseThrow(()-> new NotFoundException("Client not found"));
        return new User(client.getEmail(),client.getPassword(), List.of(new SimpleGrantedAuthority(client.getRole().toString())));
    }
}

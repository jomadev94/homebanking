package com.mindhub.homebanking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Role;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.TokenUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//BeforeAll without static method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TokenUtils tokenUtils;
    private final ObjectMapper mapper = new ObjectMapper();
    private String adminToken;
    private String clientToken;
    private final RegisterDTO toCreate=new RegisterDTO("prueba","ejemplo","prueba@hotmail.com","Sarasa123");
    private Client admin;
    private Client client;

    @BeforeAll
    public void init(){
        admin=new Client("pedro","sanchez","pedro@hotmail.com",passwordEncoder.encode("Sarasa123"), Role.ADMIN);
        client=new Client("alejo","rodriguez","ale@gmail.com",passwordEncoder.encode("Sarasa123"), Role.CLIENT);
        clientRepository.save(admin);
        clientRepository.save(client);
        adminToken="Bearer "+tokenUtils.generateToken(admin.getEmail());
        clientToken="Bearer "+tokenUtils.generateToken(client.getEmail());
    }

    @AfterAll
    public void reset(){
        clientRepository.deleteByEmail(admin.getEmail());
        clientRepository.deleteByEmail(client.getEmail());
        clientRepository.deleteByEmail(toCreate.getEmail());
    }

    @Test
    @DisplayName("Get clients")
    void getClients() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/clients")
                .header("Authorization", adminToken);
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get client by id")
    void getClientById() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders.get("/api/clients/1")
                .header("Authorization", clientToken);
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get current client")
    void getCurrentClient() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders.get("/api/clients/current")
                .header("Authorization", clientToken);
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.firstName").value("alejo"))
        ;
    }

    @Test
    @DisplayName("Register")
    void register() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(toCreate));
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

}
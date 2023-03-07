package com.mindhub.homebanking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.homebanking.dtos.RegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockHttpSession adminSession;
    private MockHttpSession clientSession;

    @BeforeEach
    public void setup() throws Exception {
        System.out.print("Starting Tests:");
        MvcResult resultAdmin = mvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content("email=admin&password=1111")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        MvcResult resultClient = mvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .content("email=melba@mindhub.com&password=1234")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        adminSession=(MockHttpSession) resultAdmin.getRequest().getSession();
        clientSession=(MockHttpSession) resultClient.getRequest().getSession();
    }

    @Test
    @DisplayName("Get clients")
    void getClients() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders.get("/api/clients").session(adminSession);
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get client by id")
    void getClientById() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders.get("/api/clients/1").session(adminSession);
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get current client")
    void getCurrentClient() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders.get("/api/clients/current").session(clientSession);
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.firstName").value("Melba"))
        ;
    }

    @Test
    @DisplayName("Register")
    void register() throws Exception {
        RequestBuilder request= MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new RegisterDTO("Esteban",
                        "Perez","esteban@gmail.com","Sarasa123")));
        mvc.perform(request).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

}
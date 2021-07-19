package com.sgorokh.TimeSaver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgorokh.TimeSaver.TimeSaverApplication;
import com.sgorokh.TimeSaver.domain.servises.ClientService;
import com.sgorokh.TimeSaver.models.Client;
import com.sgorokh.TimeSaver.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.requests.UpdateClientRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {TimeSaverApplication.class})
public class ClientControllerTest {

    @Value("${api.path}")
    private String apiPath;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ClientService clientServiceMock;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    private final Long clientId = 6L;
    private final String clientName = "testClientName";
    private final String clientEmail = "testclientemail@mail.com";
    private final String clientPhone = "+48131223132";

    private final Long clientId2 = 7L;
    private final String clientName2 = "test2ClientName";
    private final String clientEmail2 = "test2clientemail@mail.com";
    private final String clientPhone2 = "+48133333132";

    private Client client;
    private Client client2;
    private CreateClientRequest createClientRequest;
    private UpdateClientRequest updateClientRequest;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        client = Client.builder()
                .id(clientId)
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();

        client2 = Client.builder()
                .id(clientId2)
                .name(clientName2)
                .email(clientEmail2)
                .phone(clientPhone2)
                .build();

        createClientRequest = CreateClientRequest.builder()
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();

        updateClientRequest = UpdateClientRequest.builder()
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();
    }

    @Test
    public void it_should_get_clients() throws Exception {
        List<Client> clientList = Arrays.asList(client, client2);
        when(clientServiceMock.getClients()).thenReturn(clientList);

        mockMvc.perform(get(apiPath + "clients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(clientId))
                .andExpect(jsonPath("$.[0].name").value(clientName))
                .andExpect(jsonPath("$.[0].email").value(clientEmail))
                .andExpect(jsonPath("$.[0].phone").value(clientPhone))
                .andExpect(jsonPath("$.[1].id").value(clientId2))
                .andExpect(jsonPath("$.[1].name").value(clientName2))
                .andExpect(jsonPath("$.[1].email").value(clientEmail2))
                .andExpect(jsonPath("$.[1].phone").value(clientPhone2));
    }

    @Test
    public void it_should_get_client_by_id() throws Exception {
        Optional<Client> optionalClient = Optional.of(client);
        when(clientServiceMock.getClientById(clientId)).thenReturn(optionalClient);

        mockMvc.perform(get(apiPath + "clients/" + clientId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.name").value(clientName))
                .andExpect(jsonPath("$.email").value(clientEmail))
                .andExpect(jsonPath("$.phone").value(clientPhone));
    }

    @Test
    public void when_get_client_by_id_it_should_return404_when_client_not_exist() throws Exception {
        when(clientServiceMock.getClientById(clientId)).thenReturn(Optional.empty());

        mockMvc.perform(get(apiPath + "clients/" + clientId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void it_should_get_clients_by_string() throws Exception {
        List<Client> clientList = Arrays.asList(client, client2);
        when(clientServiceMock.searchByString("test")).thenReturn(clientList);

        mockMvc.perform(get(apiPath + "clients/search/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(clientId))
                .andExpect(jsonPath("$.[0].name").value(clientName))
                .andExpect(jsonPath("$.[0].email").value(clientEmail))
                .andExpect(jsonPath("$.[0].phone").value(clientPhone))
                .andExpect(jsonPath("$.[1].id").value(clientId2))
                .andExpect(jsonPath("$.[1].name").value(clientName2))
                .andExpect(jsonPath("$.[1].email").value(clientEmail2))
                .andExpect(jsonPath("$.[1].phone").value(clientPhone2));
    }

    @Test
    public void it_should_return_created_client() throws Exception {
        when(clientServiceMock.saveClient(any(CreateClientRequest.class))).thenReturn(client);

        mockMvc.perform(
                post(apiPath + "clients")
                .content(mapper.writeValueAsString(createClientRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", apiPath + "clients"))
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.name").value(clientName))
                .andExpect(jsonPath("$.email").value(clientEmail))
                .andExpect(jsonPath("$.phone").value(clientPhone));
    }

    @Test
    public void when_create_should_return400_when_client_already_exist() throws Exception {
        Client client = Client.builder()
                .id(null)
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();

        when(clientServiceMock.saveClient(any(CreateClientRequest.class))).thenReturn(client);

        mockMvc.perform(post(apiPath + "clients")
                .content(mapper.writeValueAsString(createClientRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void it_should_update_clients() throws Exception {
        when(clientServiceMock.updateClient(any(Long.class), any(UpdateClientRequest.class))).thenReturn(client);

        mockMvc.perform(
                put(apiPath + "clients/" + clientId)
                .content(mapper.writeValueAsString(updateClientRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.name").value(clientName))
                .andExpect(jsonPath("$.email").value(clientEmail))
                .andExpect(jsonPath("$.phone").value(clientPhone));
    }

    @Test
    public void when_update_it_should_return400_when_client_not_exist() throws Exception {
        when(clientServiceMock.updateClient(any(Long.class), any(UpdateClientRequest.class))).thenReturn(client);

        mockMvc.perform(
                put(apiPath + "clients/" + clientId)
                .content(mapper.writeValueAsString(updateClientRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.name").value(clientName))
                .andExpect(jsonPath("$.email").value(clientEmail))
                .andExpect(jsonPath("$.phone").value(clientPhone));
    }

    @Test
    public void when_delete_should_return_client() throws Exception {
        when(clientServiceMock.deleteClient(any(Long.class))).thenReturn(client);

        mockMvc.perform(
                delete(apiPath + "clients/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.name").value(clientName))
                .andExpect(jsonPath("$.email").value(clientEmail))
                .andExpect(jsonPath("$.phone").value(clientPhone));
    }

    @Test
    public void when_delete_should_return400_when_client_not_exist() throws Exception {
        when(clientServiceMock.deleteClient(any(Long.class))).thenReturn(null);

        mockMvc.perform(delete(apiPath + "clients/" + clientId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }











}

package com.sgorokh.TimeSaver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgorokh.TimeSaver.TimeSaverApplication;
import com.sgorokh.TimeSaver.controllers.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.controllers.requests.UpdateClientRequest;
import com.sgorokh.TimeSaver.domain.dtos.ClientDTO;
import com.sgorokh.TimeSaver.domain.dtos.ClientDetailsDTO;
import com.sgorokh.TimeSaver.domain.dtos.SessionDetailsDTO;
import com.sgorokh.TimeSaver.domain.servises.ClientService;
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

import static org.hamcrest.Matchers.hasSize;
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

    private ClientDTO client;
    private ClientDTO client2;
    private CreateClientRequest createClientRequest;
    private UpdateClientRequest updateClientRequest;

    private final Long clientDetailsId = 5L;
    private final String clientDetailsName = "testClientDetailsName";
    private final String clientDetailsEmail = "testClient@email.com";
    private final String clientDetailsPhone = "+48123111123";
    private final Long sessionDetailsId1 = 6L;
    private final Long sessionDetailsId2 = 7L;
    private final String sessionDetailsName1 = "testSessionDetailsName1";
    private final String sessionDetailsName2 = "testSessionDetailsName2";
    private final Long sessionDetailsImageId1 = 8L;
    private final Long sessionDetailsImageId2 = 9L;
    private ClientDetailsDTO clientDetailsDTO;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        SessionDetailsDTO sessionDetailsDTO1 = SessionDetailsDTO.builder()
                .id(sessionDetailsId1)
                .name(sessionDetailsName1)
                .imageId(sessionDetailsImageId1)
                .build();

        SessionDetailsDTO sessionDetailsDTO2 = SessionDetailsDTO.builder()
                .id(sessionDetailsId2)
                .name(sessionDetailsName2)
                .imageId(sessionDetailsImageId2)
                .build();

        clientDetailsDTO = ClientDetailsDTO.builder()
                .id(clientDetailsId)
                .name(clientDetailsName)
                .email(clientDetailsEmail)
                .phone(clientDetailsPhone)
                .sessionDetails(Arrays.asList(sessionDetailsDTO1, sessionDetailsDTO2))
                .build();

        client = ClientDTO.builder()
                .id(clientId)
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();

        client2 = ClientDTO.builder()
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
        List<ClientDTO> clientList = Arrays.asList(client, client2);
        when(clientServiceMock.getClients()).thenReturn(clientList);

        mockMvc.perform(get(apiPath + "clients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientDetails", hasSize(clientList.size())))
                .andExpect(jsonPath("$.clientDetails[0].id").value(clientId))
                .andExpect(jsonPath("$.clientDetails[0].name").value(clientName))
                .andExpect(jsonPath("$.clientDetails[0].email").value(clientEmail))
                .andExpect(jsonPath("$.clientDetails[0].phone").value(clientPhone))
                .andExpect(jsonPath("$.clientDetails[1].id").value(clientId2))
                .andExpect(jsonPath("$.clientDetails[1].name").value(clientName2))
                .andExpect(jsonPath("$.clientDetails[1].email").value(clientEmail2))
                .andExpect(jsonPath("$.clientDetails[1].phone").value(clientPhone2));
    }

    @Test
    public void it_should_get_client_by_id() throws Exception {
        Optional<ClientDetailsDTO> optionalClient = Optional.of(clientDetailsDTO);
        when(clientServiceMock.getClientDetailsById(clientDetailsId)).thenReturn(optionalClient);

        mockMvc.perform(get(apiPath + "clients/" + clientDetailsId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientDetailsId))
                .andExpect(jsonPath("$.name").value(clientDetailsName))
                .andExpect(jsonPath("$.email").value(clientDetailsEmail))
                .andExpect(jsonPath("$.phone").value(clientDetailsPhone))
                .andExpect(jsonPath("$.sessions", hasSize(2)))
                .andExpect(jsonPath("$.sessions[0].id").value(sessionDetailsId1))
                .andExpect(jsonPath("$.sessions[0].name").value(sessionDetailsName1))
                .andExpect(jsonPath("$.sessions[0].imageId").value(sessionDetailsImageId1))
                .andExpect(jsonPath("$.sessions[1].id").value(sessionDetailsId2))
                .andExpect(jsonPath("$.sessions[1].name").value(sessionDetailsName2))
                .andExpect(jsonPath("$.sessions[1].imageId").value(sessionDetailsImageId2));
    }

    @Test
    public void when_get_client_by_id_it_should_return404_when_client_not_exist() throws Exception {
        when(clientServiceMock.getClientDetailsById(clientDetailsId)).thenReturn(Optional.empty());

        mockMvc.perform(get(apiPath + "clients/" + clientId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void it_should_get_clients_by_string() throws Exception {
        List<ClientDTO> clientList = Arrays.asList(client, client2);
        when(clientServiceMock.searchByString("test")).thenReturn(clientList);

        mockMvc.perform(get(apiPath + "clients/search/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientDetails", hasSize(clientList.size())))
                .andExpect(jsonPath("$.clientDetails[0].id").value(clientId))
                .andExpect(jsonPath("$.clientDetails[0].name").value(clientName))
                .andExpect(jsonPath("$.clientDetails[0].email").value(clientEmail))
                .andExpect(jsonPath("$.clientDetails[0].phone").value(clientPhone))
                .andExpect(jsonPath("$.clientDetails[1].id").value(clientId2))
                .andExpect(jsonPath("$.clientDetails[1].name").value(clientName2))
                .andExpect(jsonPath("$.clientDetails[1].email").value(clientEmail2))
                .andExpect(jsonPath("$.clientDetails[1].phone").value(clientPhone2));
    }

    @Test
    public void it_should_return_name_of_created_client() throws Exception {
        when(clientServiceMock.saveClient(any(ClientDTO.class))).thenReturn(client);

        mockMvc.perform(
                post(apiPath + "clients")
                        .content(mapper.writeValueAsString(createClientRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", apiPath + "clients"))
                .andExpect(jsonPath("$.name").value(clientName));
    }

    @Test
    public void when_create_should_return400_when_client_already_exist() throws Exception {
        when(clientServiceMock.saveClient(any(ClientDTO.class))).thenReturn(null);

        mockMvc.perform(post(apiPath + "clients")
                .content(mapper.writeValueAsString(createClientRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void it_should_update_clients() throws Exception {
        when(clientServiceMock.updateClient(any(ClientDTO.class))).thenReturn(client);

        mockMvc.perform(
                put(apiPath + "clients")
                        .content(mapper.writeValueAsString(updateClientRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(clientName));
    }

    @Test
    public void when_update_it_should_return400_when_client_not_exist() throws Exception {
        when(clientServiceMock.updateClient(any(ClientDTO.class))).thenReturn(null);

        mockMvc.perform(
                put(apiPath + "clients")
                        .content(mapper.writeValueAsString(updateClientRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_delete_should_return_client_name() throws Exception {
        when(clientServiceMock.deleteClient(any(Long.class))).thenReturn(client);

        mockMvc.perform(
                delete(apiPath + "clients/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(client.getName()));
    }

    @Test
    public void when_delete_should_return400_when_client_not_exist() throws Exception {
        when(clientServiceMock.deleteClient(any(Long.class))).thenReturn(null);

        mockMvc.perform(delete(apiPath + "clients/" + clientId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}

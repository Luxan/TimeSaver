package com.sgorokh.TimeSaver.domain.services;

import com.sgorokh.TimeSaver.controllers.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.controllers.requests.UpdateClientRequest;
import com.sgorokh.TimeSaver.domain.dtos.ClientDTO;
import com.sgorokh.TimeSaver.domain.dtos.ClientDetailsDTO;
import com.sgorokh.TimeSaver.domain.servises.ClientService;
import com.sgorokh.TimeSaver.models.Client;
import com.sgorokh.TimeSaver.models.Image;
import com.sgorokh.TimeSaver.models.Session;
import com.sgorokh.TimeSaver.repositories.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

    @Mock
    ClientRepository clientRepositoryMock;

    @InjectMocks
    ClientService clientService;

    private final Long clientId = 6L;
    private final String clientName = "testClientName";
    private final String clientEmail = "testclientemail@mail.com";
    private final String clientPhone = "+48131223132";

    private final Long clientId2 = 7L;
    private final String clientName2 = "test2ClientName";
    private final String clientEmail2 = "test2clientemail@mail.com";
    private final String clientPhone2 = "+48133333132";

    private ClientDTO clientDto;
    private CreateClientRequest createClientRequest;
    private UpdateClientRequest updateClientRequest;

    private Client client1;
    private Client client2;
    private Long sessionId = 8L;
    private String sessionName = "testSessionName";
    private Long imageId = 9L;

    @Before()
    public void setUp() {
        clientDto = ClientDTO.builder()
                .id(clientId)
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();

        createClientRequest = CreateClientRequest.builder()
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();

        updateClientRequest = UpdateClientRequest.builder()
                .name(clientName2)
                .email(clientEmail2)
                .phone(clientPhone2)
                .build();

        Session session = Session.builder()
                .id(sessionId)
                .name(sessionName)
                .images(Collections.singletonList(Image.builder()
                        .id(imageId)
                        .build()))
                .build();

        client1 = Client.builder()
                .id(clientId)
                .name(clientName)
                .phone(clientPhone)
                .email(clientEmail)
                .sessions(Collections.singletonList(session))
                .build();

        client2 = Client.builder()
                .id(clientId2)
                .name(clientName2)
                .phone(clientPhone2)
                .email(clientEmail2)
                .sessions(new ArrayList<>())
                .build();
    }

    @Test
    public void when_retrieve_clients_it_should_retrieve_clients() {
        List<Client> clientList = Arrays.asList(client1, client2);
        when(clientRepositoryMock.findAll()).thenReturn(clientList);

        List<ClientDTO> returnedClients = clientService.getClients();

        assertThat(returnedClients.size()).isEqualTo(clientList.size());

        assertThat(returnedClients.get(0).getId()).isEqualTo(clientId);
        assertThat(returnedClients.get(0).getName()).isEqualTo(clientName);
        assertThat(returnedClients.get(0).getPhone()).isEqualTo(clientPhone);
        assertThat(returnedClients.get(0).getEmail()).isEqualTo(clientEmail);

        assertThat(returnedClients.get(1).getId()).isEqualTo(clientId2);
        assertThat(returnedClients.get(1).getName()).isEqualTo(clientName2);
        assertThat(returnedClients.get(1).getPhone()).isEqualTo(clientPhone2);
        assertThat(returnedClients.get(1).getEmail()).isEqualTo(clientEmail2);
    }

    @Test
    public void when_retrieve_clients_by_id_it_should_retrieve_client() {
        Optional<Client> optionalClient = Optional.of(client1);

        when(clientRepositoryMock.findById(clientId)).thenReturn(optionalClient);

        Optional<ClientDetailsDTO> returnedOptionalClient = clientService.getClientDetailsById(clientId);

        assertThat(returnedOptionalClient).isPresent();
        assertThat(returnedOptionalClient.get().getId()).isEqualTo(clientId);
        assertThat(returnedOptionalClient.get().getName()).isEqualTo(clientName);
        assertThat(returnedOptionalClient.get().getPhone()).isEqualTo(clientPhone);
        assertThat(returnedOptionalClient.get().getEmail()).isEqualTo(clientEmail);
        assertThat(returnedOptionalClient.get().getSessionDetails().size()).isEqualTo(1);
        assertThat(returnedOptionalClient.get().getSessionDetails().get(0).getId()).isEqualTo(sessionId);
        assertThat(returnedOptionalClient.get().getSessionDetails().get(0).getName()).isEqualTo(sessionName);
        assertThat(returnedOptionalClient.get().getSessionDetails().get(0).getImageId()).isEqualTo(imageId);
    }

    @Test
    public void when_search_clients_by_string_it_should_retrieve_clients() {
        List<Client> clientList = Arrays.asList(client1, client2);
        String testString = "test";

        when(clientRepositoryMock.searchByString(testString)).thenReturn(clientList);

        List<ClientDTO> returnedClients = clientService.searchByString(testString);

        assertThat(returnedClients.size()).isEqualTo(clientList.size());
        assertThat(returnedClients.get(0).getId()).isEqualTo(clientId);
        assertThat(returnedClients.get(0).getName()).isEqualTo(clientName);
        assertThat(returnedClients.get(0).getEmail()).isEqualTo(clientEmail);
        assertThat(returnedClients.get(0).getPhone()).isEqualTo(clientPhone);
        assertThat(returnedClients.get(1).getId()).isEqualTo(clientId2);
        assertThat(returnedClients.get(1).getName()).isEqualTo(clientName2);
        assertThat(returnedClients.get(1).getEmail()).isEqualTo(clientEmail2);
        assertThat(returnedClients.get(1).getPhone()).isEqualTo(clientPhone2);
    }

    @Test
    public void when_save_client_it_should_return_client() {
        when(clientRepositoryMock.searchByName(clientName)).thenReturn(null);
        when(clientRepositoryMock.save(any(Client.class))).thenReturn(client1);

        ClientDTO created = clientService.saveClient(clientDto);

        assertThat(created.getId()).isEqualTo(clientId);
        assertThat(created.getName()).isEqualTo(clientName);
        assertThat(created.getEmail()).isEqualTo(clientEmail);
        assertThat(created.getPhone()).isEqualTo(clientPhone);
    }

    @Test
    public void when_save_client_it_should_return_null_if_already_exist() {
        when(clientRepositoryMock.searchByName(any(String.class))).thenReturn(client1);

        ClientDTO created = clientService.saveClient(clientDto);

        assertThat(created).isNull();
    }

    @Test
    public void when_update_client_it_should_retrieve_client() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(client1));
        when(clientRepositoryMock.save(any(Client.class))).thenAnswer((Answer<Client>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Client) args[0];
        });

        ClientDTO returnedClient = clientService.updateClient(clientDto);

        assertThat(returnedClient.getId()).isEqualTo(clientId);
        assertThat(returnedClient.getName()).isEqualTo(clientName);
        assertThat(returnedClient.getEmail()).isEqualTo(clientEmail);
        assertThat(returnedClient.getPhone()).isEqualTo(clientPhone);
    }


    @Test
    public void when_update_client_it_should_return_null_if_client_not_exist() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.empty());

        ClientDTO returnedClient = clientService.updateClient(clientDto);

        assertThat(returnedClient).isNull();
    }

    @Test
    public void when_delete_client_it_should_retrieve_clientId() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(client1));

        ClientDTO client = clientService.deleteClient(clientId);

        assertThat(client.getId()).isEqualTo(clientId);
        assertThat(client.getName()).isEqualTo(clientName);
        assertThat(client.getEmail()).isEqualTo(clientEmail);
        assertThat(client.getPhone()).isEqualTo(clientPhone);
    }

    @Test
    public void when_delete_client_it_should_return_null_if_client_not_exist() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.empty());

        ClientDTO client = clientService.deleteClient(clientId);

        assertThat(client).isNull();
    }
}

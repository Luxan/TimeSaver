package com.sgorokh.TimeSaver.domain.services;

import com.sgorokh.TimeSaver.domain.servises.ClientService;
import com.sgorokh.TimeSaver.models.Client;
import com.sgorokh.TimeSaver.repositories.ClientRepository;
import com.sgorokh.TimeSaver.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.requests.UpdateClientRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private final String clientName2 = "test2ClientName";
    private final String clientEmail2 = "test2clientemail@mail.com";
    private final String clientPhone2 = "+48133333132";
    private Client client;
    private CreateClientRequest createClientRequest;
    private UpdateClientRequest updateClientRequest;

    @Before()
    public void setUp() {
        client = Client.builder()
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
    }

    @Test
    public void when_retrieve_clients_it_should_retrieve_clients() {
        List<Client> clientList = Arrays.asList(new Client(), new Client());
        when(clientRepositoryMock.findAll()).thenReturn(clientList);

        List<Client> returnedClients = clientService.getClients();

        assertThat(clientList).isEqualTo(returnedClients);
    }

    @Test
    public void when_retrieve_clients_by_id_it_should_retrieve_client() {
        Optional<Client> optionalClient = Optional.of(new Client());

        when(clientRepositoryMock.findById(clientId)).thenReturn(optionalClient);

        Optional<Client> returnedOptionalClient = clientService.getClientById(clientId);

        assertThat(optionalClient).isEqualTo(returnedOptionalClient);
    }

    @Test
    public void when_search_clients_by_string_it_should_retrieve_clients() {
        List<Client> clientList = Arrays.asList(new Client(), new Client());
        String testString = "test";

        when(clientRepositoryMock.searchByString(testString)).thenReturn(clientList);

        List<Client> returnedClients = clientService.searchByString(testString);

        assertThat(clientList).isEqualTo(returnedClients);
    }

    @Test
    public void when_save_client_it_should_return_client() {
        when(clientRepositoryMock.save(any(Client.class))).thenReturn(client);

        Client created = clientService.saveClient(createClientRequest);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isSameAs(createClientRequest.getName());
        assertThat(created.getEmail()).isSameAs(createClientRequest.getEmail());
        assertThat(created.getPhone()).isSameAs(createClientRequest.getPhone());
    }

    @Test
    public void when_save_client_it_should_return_client_with_id_equals_null_if_already_exist() {
        Client client = Client.builder()
                .id(null)
                .name(clientName)
                .email(clientEmail)
                .phone(clientPhone)
                .build();

        when(clientRepositoryMock.save(any(Client.class))).thenReturn(client);

        Client created = clientService.saveClient(createClientRequest);

        assertThat(created.getId()).isNull();
        assertThat(created.getName()).isSameAs(createClientRequest.getName());
        assertThat(created.getEmail()).isSameAs(createClientRequest.getEmail());
        assertThat(created.getPhone()).isSameAs(createClientRequest.getPhone());
    }

    @Test
    public void when_update_client_it_should_retrieve_client() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepositoryMock.save(any(Client.class))).thenAnswer((Answer<Client>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Client) args[0];
        });

        Client returnedClient = clientService.updateClient(clientId, updateClientRequest);

        assertThat(returnedClient.getId()).isEqualTo(clientId);
        assertThat(returnedClient.getName()).isEqualTo(clientName2);
        assertThat(returnedClient.getEmail()).isEqualTo(clientEmail2);
        assertThat(returnedClient.getPhone()).isEqualTo(clientPhone2);
    }


    @Test
    public void when_update_client_it_should_return_null_if_client_not_exist() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.empty());

        Client returnedClient = clientService.updateClient(clientId, updateClientRequest);

        assertThat(returnedClient).isNull();
    }

    @Test
    public void when_delete_client_it_should_retrieve_clientId() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(client));

        Client client = clientService.deleteClient(clientId);

        assertThat(client.getId()).isEqualTo(clientId);
        assertThat(client.getName()).isEqualTo(clientName);
        assertThat(client.getEmail()).isEqualTo(clientEmail);
        assertThat(client.getPhone()).isEqualTo(clientPhone);
    }

    @Test
    public void when_delete_client_it_should_return_null_if_client_not_exist() {
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.empty());

        Client client = clientService.deleteClient(clientId);

        assertThat(client).isNull();
    }
}

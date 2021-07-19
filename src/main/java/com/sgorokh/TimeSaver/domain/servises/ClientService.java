package com.sgorokh.TimeSaver.domain.servises;

import com.sgorokh.TimeSaver.exceptions.InvalidRequestException;
import com.sgorokh.TimeSaver.exceptions.ResourceNotFoundException;
import com.sgorokh.TimeSaver.models.Client;
import com.sgorokh.TimeSaver.repositories.ClientRepository;
import com.sgorokh.TimeSaver.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.requests.UpdateClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long clientId) {
        return clientRepository.findById(clientId);
    }

    public List<Client> searchByString(String searchString) {
        return clientRepository.searchByString(searchString);
    }

    public Client saveClient(CreateClientRequest request) {
        Client foundClient = clientRepository.searchByName(request.getName());
        Client client = Client.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
        if (foundClient != null) {
            if (foundClient.getEmail().equals(request.getEmail()) ||
                    foundClient.getPhone().equals(request.getPhone()))
                return client;
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Long clientId, UpdateClientRequest request) {
        Optional<Client> foundClient = clientRepository.findById(clientId);
        if (!foundClient.isPresent()) return null;
        Client client = foundClient.get();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());

        return clientRepository.save(client);
    }

    public Client deleteClient(Long clientId) {
        Optional<Client> foundClient = clientRepository.findById(clientId);
        if (!foundClient.isPresent()) return null;
        Client client = foundClient.get();
        clientRepository.delete(client);
        return client;
    }
}

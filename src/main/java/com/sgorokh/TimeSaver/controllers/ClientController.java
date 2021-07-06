package com.sgorokh.TimeSaver.controllers;

import com.sgorokh.TimeSaver.exceptions.ResourceNotFoundException;
import com.sgorokh.TimeSaver.models.Client;
import com.sgorokh.TimeSaver.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("clients")
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @GetMapping("clients/{id}")
    public Client getClientById(@PathVariable(value = "id") Long clientId) throws ResourceNotFoundException {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Client by id = " + clientId));
    }

    @PutMapping("clients/search/{searchString}")
    public List<Client> getClientsByName(@PathVariable(value = "searchString") String searchString) {
        return clientRepository.searchByString(searchString);
    }

    @PostMapping("clients")
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("clients/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable(value = "id") Long clientId,
                                               @Valid @RequestBody Client client) throws ResourceNotFoundException {
        Client foundClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Client by id = " + clientId));
        foundClient.setName(client.getName());
        foundClient.setEmail(client.getEmail());

        return ResponseEntity.ok(clientRepository.save(foundClient));
    }

    @DeleteMapping("clients/{id}")
    public Map<String, Boolean> deleteClient(@PathVariable(value = "id") Long clientId) throws ResourceNotFoundException {
        Client foundClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not get Client by id = " + clientId));
        clientRepository.delete(foundClient);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}

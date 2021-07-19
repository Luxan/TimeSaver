package com.sgorokh.TimeSaver.controllers;

import com.sgorokh.TimeSaver.domain.servises.ClientService;
import com.sgorokh.TimeSaver.exceptions.InvalidRequestException;
import com.sgorokh.TimeSaver.exceptions.ResourceNotFoundException;
import com.sgorokh.TimeSaver.models.Client;
import com.sgorokh.TimeSaver.requests.CreateClientRequest;
import com.sgorokh.TimeSaver.requests.UpdateClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${api.path}")
public class ClientController {

    @Value("${api.path}")
    private String apiPath;

    private final ClientService clientService;

    @Autowired
    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("clients")
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable(value = "id") Long clientId)
            throws ResourceNotFoundException {
        Optional<Client> clientById = clientService.getClientById(clientId);
        Client client = clientById.orElseThrow(() ->
                new ResourceNotFoundException("Could not get Client by id = " + clientId));
        return ResponseEntity.ok(client);
    }

    @GetMapping("clients/search/{searchString}")
    public ResponseEntity<List<Client>> getClientsByString(@PathVariable(value = "searchString") String searchString) {
        return ResponseEntity.ok(clientService.searchByString(searchString));
    }

    @PostMapping("clients")
    public ResponseEntity<Client> createClient(@RequestBody CreateClientRequest clientRequest,
                                               WebRequest request) throws URISyntaxException, InvalidRequestException {
        Client client = clientService.saveClient(clientRequest);
        if (client.getId() == null) {
            throw new InvalidRequestException("Client with this phone number or name already exist: "
                    + client.getName() + " " + client.getPhone());
        }
        URI uri = new URI(request.getContextPath() + apiPath + "clients");
        return ResponseEntity.created(uri).body(client);
    }

    @PutMapping("clients/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable(value = "id") Long clientId,
                                               @Valid @RequestBody UpdateClientRequest request)
            throws InvalidRequestException {
        Client client = clientService.updateClient(clientId, request);
        if (client == null) throw new InvalidRequestException("Client with this id does not exist" + clientId);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("clients/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable(value = "id") Long clientId)
            throws InvalidRequestException {
        Client deletedClient = clientService.deleteClient(clientId);
        Map<String, Boolean> response = new HashMap<>();
        if (deletedClient == null)
            throw new InvalidRequestException("Client with this id does not exist: " + clientId);
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(deletedClient);
    }
}
